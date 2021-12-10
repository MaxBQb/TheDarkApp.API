package lab.maxb.dark_api.Controllers
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.DB.DAO.RecognitionTaskDAO
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.RecognitionTaskDTOCreation
import lab.maxb.dark_api.SECURITY_SCHEME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("task")
@SecurityRequirement(name = SECURITY_SCHEME)
class RecognitionTaskController @Autowired constructor(
    private val recognitionTaskDAO: RecognitionTaskDAO,
    private val userDAO: UserDAO,
) {

    @GetMapping("/all")
    fun getAllRecognitionTasks()
        = recognitionTaskDAO.findByOrderByReviewedAsc()

    @GetMapping("/byReview")
    fun getAllRecognitionTasksByReview(@RequestParam isReviewed: Boolean)
        = recognitionTaskDAO.findByReviewedEquals(isReviewed)

    @GetMapping("/byId")
    fun getRecognitionTask(@RequestParam id: UUID): RecognitionTask?
        = recognitionTaskDAO.findByIdEquals(id)

    @PostMapping("/add")
    fun addRecognitionTask(@RequestBody task: RecognitionTaskDTOCreation)
        = userDAO.findByIdEquals(task.owner_id)?.let {
            recognitionTaskDAO.save(task.toRecognitionTask(it)).id
        }

}
