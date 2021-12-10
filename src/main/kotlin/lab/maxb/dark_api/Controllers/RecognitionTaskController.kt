package lab.maxb.dark_api.Controllers
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.DB.DAO.RecognitionTaskDAO
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskFullView
import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.hasControlPrivileges
import lab.maxb.dark_api.Model.isUser
import lab.maxb.dark_api.SECURITY_SCHEME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("task")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@SecurityRequirement(name = SECURITY_SCHEME)
class RecognitionTaskController @Autowired constructor(
    private val recognitionTaskDAO: RecognitionTaskDAO,
    private val userDAO: UserDAO,
) {

    @GetMapping("/all")
    fun getAllRecognitionTasks(auth: Authentication)
        = if (auth.role.isUser)
            recognitionTaskDAO.findByReviewedTrue()
        else if (auth.role.hasControlPrivileges)
            recognitionTaskDAO.findByOrderByReviewedAsc()
        else null

    @GetMapping("/{id}")
    fun getRecognitionTask(auth: Authentication, @PathVariable id: UUID): RecognitionTaskFullView?
        = if (auth.role.isUser)
            recognitionTaskDAO.findByIdEqualsAndReviewedTrue(id)
        else if (auth.role.hasControlPrivileges)
            recognitionTaskDAO.findByIdEquals(id, RecognitionTaskFullView::class.java)
        else null

    @RolesAllowed("MODERATOR")
    @GetMapping("mark/{id}")
    fun markRecognitionTask(auth: Authentication,
                            @PathVariable id: UUID,
                            @RequestParam isAllowed: Boolean)
        = if (isAllowed)
            recognitionTaskDAO.findByIdEquals(id, RecognitionTask::class.java)?.let {
                it.reviewed = true
                recognitionTaskDAO.save(it)
                true
            } ?: false
        else {
            recognitionTaskDAO.deleteById(id)
            true
        }


    @PostMapping("/add")
    fun addRecognitionTask(@RequestBody task: RecognitionTaskCreationDTO)
        = userDAO.findByIdEquals(task.owner_id)?.let {
            recognitionTaskDAO.save(RecognitionTask(
                task.names,
                task.images,
                it
            )).id
        }

}
