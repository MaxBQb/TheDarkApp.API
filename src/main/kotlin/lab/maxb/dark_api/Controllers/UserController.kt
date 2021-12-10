package lab.maxb.dark_api.Controllers
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.User
import lab.maxb.dark_api.SECURITY_SCHEME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("user")
@SecurityRequirement(name = SECURITY_SCHEME)
class UserController @Autowired constructor(
    private val userDAO: UserDAO,
) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): User?
        = userDAO.findByIdEquals(id)

}
