package lab.maxb.dark_api.domain.service.implementation

import lab.maxb.dark_api.domain.exceptions.AccessDeniedException
import lab.maxb.dark_api.domain.exceptions.NotFoundException
import lab.maxb.dark_api.domain.exceptions.withValidation
import lab.maxb.dark_api.domain.gateway.RecognitionTasksGateway
import lab.maxb.dark_api.domain.gateway.SolutionsGateway
import lab.maxb.dark_api.domain.model.*
import lab.maxb.dark_api.domain.service.ImagesService
import lab.maxb.dark_api.domain.service.TasksService
import lab.maxb.dark_api.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class TasksServiceImpl @Autowired constructor(
    private val tasksGateway: RecognitionTasksGateway,
    private val solutionsGateway: SolutionsGateway,
    private val userService: UserService,
    private val imagesService: ImagesService,
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

    override fun mark(id: UUID, isAllowed: Boolean) {
        val task = tasksGateway.findById(id) ?: throw NotFoundException.of("Task")
        if (!isAllowed && !task.reviewed) {
            tasksGateway.findById(id)?.images?.forEach { path ->
                runCatching { imagesService.delete(path) }
            }
            tasksGateway.deleteById(id)
        } else
            tasksGateway.save(task.copy(reviewed = isAllowed))
    }

    override fun add(task: RecognitionTask): RecognitionTask {
        val validModel = task.validate()
        withValidation {
            task.images.forEach {
                if (!imagesService.exists(it))
                    addError("Image $it unavailable")
            }
        }
        return tasksGateway.save(validModel)
    }

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
                answer, rating, account.user, task
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
