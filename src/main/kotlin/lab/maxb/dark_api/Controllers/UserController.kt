package lab.maxb.dark_api.Controllers
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("user")
class UserController @Autowired constructor(
    private val userDAO: UserDAO,
) {

    @GetMapping("/byId")
    fun getUser(@RequestParam id: UUID): User?
        = userDAO.findByIdEquals(id)

}
