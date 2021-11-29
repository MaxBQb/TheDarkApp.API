package lab.maxb.dark_api.Controllers
import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.User
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("task")
class RecognitionTaskController {
    private val tasks = mutableListOf(
        RecognitionTask(
            setOf("стул", "кресло", "диван"),
            listOf("image1", "image2"),
            User("Max", 0),
            true
        ),
        RecognitionTask(
            setOf("стол", "стол на трёх ножках", "столешница"),
            listOf("image3", "image4"),
            User("Max", 0),
            false
        ),
        RecognitionTask(
            setOf("стул1", "кресло1", "диван1"),
            listOf("image11", "image21"),
            User("Max", 0),
            true
        ),
        RecognitionTask(
            setOf("стол2", "стол на трёх ножках2", "столешница2"),
            listOf("image32", "image42"),
            User("Max", 0),
            false
        ),
    )

    @GetMapping("/all")
    fun getAllRecognitionTasks(): List<RecognitionTask>?
        = tasks.sortedBy { it.reviewed }

    @GetMapping("/byReview")
    fun getAllRecognitionTasksByReview(@RequestParam isReviewed: Boolean): List<RecognitionTask>?
        = tasks.filter { it.reviewed == isReviewed }

    @GetMapping("/byId")
    fun getRecognitionTask(@RequestParam id: UUID): RecognitionTask?
        = tasks.find { it.id == id }

    @PostMapping("/add")
    fun addRecognitionTask(@RequestBody task: RecognitionTask)
        = if (tasks.add(task)) task
          else null

}