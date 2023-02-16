package lab.maxb.dark_api.domain.service.implementation

import com.google.common.io.ByteStreams.toByteArray
import lab.maxb.dark_api.domain.gateway.RecognitionTasksGateway
import lab.maxb.dark_api.domain.gateway.SolutionsGateway
import lab.maxb.dark_api.domain.model.RecognitionTask
import lab.maxb.dark_api.domain.model.Solution
import lab.maxb.dark_api.domain.model.hasControlPrivileges
import lab.maxb.dark_api.domain.model.isUser
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.domain.service.ImageService
import lab.maxb.dark_api.domain.service.TasksService
import lab.maxb.dark_api.domain.service.UserService
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
    private val tasksGateway: RecognitionTasksGateway,
    private val solutionsGateway: SolutionsGateway,
    private val authService: AuthService,
    private val userService: UserService,
    private val imageService: ImageService,
) : TasksService {

    override fun getAvailable(auth: Authentication, pageable: Pageable)
        = if (auth.role.isUser)
            authService.getUserId(auth.name)?.let {
                tasksGateway.findUserSpecific(it, pageable)
            }
        else if (auth.role.hasControlPrivileges)
            tasksGateway.findWithNotReviewedPrioritized(pageable)
        else null

    override fun getTask(auth: Authentication, id: UUID)
        = if (auth.role.isUser)
            authService.getUserId(auth.name)?.let { userId ->
                tasksGateway.findByIdUserSpecific(id, userId)
            }
        else if (auth.role.hasControlPrivileges)
            tasksGateway.findById(id)
        else null

    override fun mark(auth: Authentication, id: UUID, isAllowed: Boolean): Boolean {
        if (!auth.role.hasControlPrivileges)
            return false
        return tasksGateway.findById(id)?.let {
            if (!isAllowed && !it.reviewed) {
                tasksGateway.findById(id)?.images?.forEach { path ->
                    runCatching { imageService.delete(path) }
                }
                tasksGateway.deleteById(id)
            } else {
                tasksGateway.save(it.copy(reviewed = isAllowed))
            }
            true
        } ?: false
    }

    override fun uploadImage(
        auth: Authentication,
        id: UUID,
        file: MultipartFile
    ): String? {
        val task = tasksGateway.findById(id) ?: return null
        val ownerId = authService.getUserId(auth.name) ?: return null
        if (ownerId != task.owner.id)
            return null

        if (task.images.count() >= RecognitionTask.MAX_IMAGES_COUNT)
            return null

        return try {
            val fileName = imageService.save(file)
            val newImagesList = task.images.toMutableList().apply {
                add(fileName)
            }
            tasksGateway.save(task.copy(images = newImagesList.toList()))
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
        task: RecognitionTask,
    ) = authService.getUser(auth.name)?.let {
        tasksGateway.save(
            RecognitionTask(
                names = task.names,
                owner = it,
            )
        ).id
    }

    override fun solve(
        auth: Authentication,
        id: UUID,
        answer: String
    ) = authService.getUser(auth.name)?.let { user ->
        val task = tasksGateway.findById(id) ?: return null
        if (task.owner.id == user.id || !task.reviewed)
            return null
        !solutionsGateway.existsByTaskIdAndUserId(id, user.id) &&
        (checkAnswer(task, answer)?.let { rating ->
            solutionsGateway.save(Solution(
                answer, rating, user, task.id
            ))
            userService.addRating(user.id, rating)
            true
        } ?: false)
    }

    private fun checkAnswer(task: RecognitionTask, answer: String)
        = if (answer in task.names) {
            1 // TODO: Можно задать разные оценки
        } else null
}