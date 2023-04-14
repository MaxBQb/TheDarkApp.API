package lab.maxb.dark_api.domain.service.implementation

import com.google.common.io.ByteStreams.toByteArray
import lab.maxb.dark_api.domain.exceptions.AccessDeniedException
import lab.maxb.dark_api.domain.exceptions.NotFoundException
import lab.maxb.dark_api.domain.exceptions.UnknownError
import lab.maxb.dark_api.domain.exceptions.ValidationError
import lab.maxb.dark_api.domain.gateway.RecognitionTasksGateway
import lab.maxb.dark_api.domain.gateway.SolutionsGateway
import lab.maxb.dark_api.domain.model.*
import lab.maxb.dark_api.domain.service.ImageService
import lab.maxb.dark_api.domain.service.TasksService
import lab.maxb.dark_api.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class TasksServiceImpl @Autowired constructor(
    private val tasksGateway: RecognitionTasksGateway,
    private val solutionsGateway: SolutionsGateway,
    private val userService: UserService,
    private val imageService: ImageService,
) : TasksService {

    override fun getAvailable(account: ShortUserCredentials, pageable: Pageable)
        = if (account.role.isUser)
            tasksGateway.findUserSpecific(account.user.id, pageable)
        else if (account.role.hasControlPrivileges)
            tasksGateway.findWithNotReviewedPrioritized(pageable)
        else throw AccessDeniedException()

    override fun getTask(account: ShortUserCredentials, id: UUID)
        = (if (account.role.isUser)
            tasksGateway.findByIdUserSpecific(id, account.user.id)
        else if (account.role.hasControlPrivileges)
            tasksGateway.findById(id)
        else throw AccessDeniedException()) ?: throw NotFoundException.of("Task")

    override fun mark(id: UUID, isAllowed: Boolean) = tasksGateway.findById(id)?.let {
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

    override fun uploadImage(
        account: ShortUserCredentials,
        id: UUID,
        file: MultipartFile
    ): String {
        val task = tasksGateway.findById(id) ?: throw NotFoundException.of("Task")
        if (account.user.id != task.owner.id)
            throw AccessDeniedException("Only task owner can perform this task")

        if (task.images.count() >= RecognitionTask.MAX_IMAGES_COUNT)
            throw ValidationError("Images amount exceeded (${RecognitionTask.MAX_IMAGES_COUNT})")

        return try {
            val fileName = imageService.save(file)
            val newImagesList = task.images.toMutableList().apply {
                add(fileName)
            }
            tasksGateway.save(task.copy(images = newImagesList.toList()))
            fileName
        } catch (e: IOException) {
            e.printStackTrace()
            throw UnknownError(e.message ?: "No extra details")
        }
    }

    override fun downloadImage(path: String) = imageService.get(path)?.use {
        ByteArrayResource(toByteArray(it))
    } ?: throw NotFoundException.of("Image")

    override fun add(task: RecognitionTask) = tasksGateway.save(task)

    override fun solve(
        account: ShortUserCredentials,
        id: UUID,
        answer: String
    ): Boolean {
        val task = tasksGateway.findById(id) ?: throw NotFoundException.of("Task")
        if (task.owner.id == account.user.id || !task.reviewed)
            throw AccessDeniedException()
        return !solutionsGateway.existsByTaskIdAndUserId(id, account.user.id) &&
         (checkAnswer(task, answer)?.let { rating ->
            solutionsGateway.save(Solution(
                answer, rating, account.user, task.id
            ))
            userService.addRating(account.user.id, rating)
            true
        } ?: false)
    }

    private fun checkAnswer(task: RecognitionTask, answer: String)
        = if (answer in task.names) {
            1 // TODO: Можно задать разные оценки
        } else null
}
