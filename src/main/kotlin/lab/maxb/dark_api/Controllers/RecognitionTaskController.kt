package lab.maxb.dark_api.Controllers
import lab.maxb.dark_api.DB.DAO.RecognitionTaskDAO
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.RecognitionTaskDTOCreation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("task")
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
        = task.owner_id?.let { owner_id ->
            userDAO.findByIdEquals(owner_id)?.let {
                recognitionTaskDAO.save(task.toRecognitionTask(it)).id
            }
        }

}
