package lab.maxb.dark_api.services.implementation

import com.google.common.io.ByteStreams.toByteArray
import lab.maxb.dark_api.controllers.role
import lab.maxb.dark_api.model.RecognitionTask
import lab.maxb.dark_api.model.hasControlPrivileges
import lab.maxb.dark_api.model.isUser
import lab.maxb.dark_api.model.pojo.RecognitionTaskCreationDTO
import lab.maxb.dark_api.model.pojo.RecognitionTaskFullView
import lab.maxb.dark_api.model.pojo.RecognitionTaskImages
import lab.maxb.dark_api.repository.dao.RecognitionTaskDAO
import lab.maxb.dark_api.repository.dao.findByIdEquals
import lab.maxb.dark_api.services.AuthService
import lab.maxb.dark_api.services.ImageService
import lab.maxb.dark_api.services.TasksService
import lab.maxb.dark_api.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class TasksServiceImpl @Autowired constructor(
    private val dataSource: RecognitionTaskDAO,
    private val authService: AuthService,
    private val userService: UserService,
    private val imageService: ImageService,
) : TasksService {

    override fun getAvailable(auth: Authentication, pageable: Pageable)
        = if (auth.role.isUser)
            authService.getUserId(auth.name)?.let {
                dataSource.findByReviewedTrueAndOwnerIdNot(it)
            }
        else if (auth.role.hasControlPrivileges)
            dataSource.findByOrderByReviewedAsc(pageable)
        else null

    override fun get(auth: Authentication, id: UUID)
        = if (auth.role.isUser)
            dataSource.findByIdEqualsAndReviewedTrue(id)
        else if (auth.role.hasControlPrivileges)
            dataSource.findByIdEquals<RecognitionTaskFullView>(id)
        else null

    override fun mark(auth: Authentication, id: UUID, isAllowed: Boolean): Boolean {
        if (!auth.role.hasControlPrivileges)
            return false
        return dataSource.findByIdEquals(id, RecognitionTask::class.java)?.let {
            if (!isAllowed && !it.reviewed) {
                dataSource.findByIdEquals<RecognitionTaskImages>(id)?.images?.forEach { path ->
                    runCatching { imageService.delete(path) }
                }
                dataSource.deleteById(id)
            } else {
                it.reviewed = isAllowed
                dataSource.save(it)
            }
            true
        } ?: false
    }

    override fun uploadImage(
        auth: Authentication,
        id: UUID,
        file: MultipartFile
    ): String? {
        val task = dataSource.findByIdEqualsAndOwner_IdEquals(
            id, authService.getUserId(auth.name) ?: return null
        ) ?: return null

        if (task.images!!.count() >= RecognitionTask.MAX_IMAGES_COUNT)
            return null

        return try {
            val fileName = imageService.save(file)
            task.images!!.add(fileName)
            dataSource.save(task)
            fileName
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun downloadImage(path: String) = imageService.get(path)?.use {
        ByteArrayResource(toByteArray(it))
    }

    override fun add(
        auth: Authentication,
        task: RecognitionTaskCreationDTO
    ) = authService.getUser(auth.name)?.let {
        dataSource.save(
            RecognitionTask(
                task.names,
                mutableListOf(),
                it
            )
        ).id
    }

    override fun solve(
        auth: Authentication,
        id: UUID,
        answer: String
    ) = authService.getUser(auth.name)?.let { user ->
        dataSource.findByIdEquals<RecognitionTask>(id)?.let { task ->
            if (task.owner!!.id == user.id || !task.reviewed)
                null
            else if (answer in task.names!!) {
                userService.addRating(user.id)
                true
            } else false
        }
    }
}