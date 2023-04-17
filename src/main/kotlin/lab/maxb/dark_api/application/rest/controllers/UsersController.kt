package lab.maxb.dark_api.application.rest.controllers
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.SECURITY_SCHEME
import lab.maxb.dark_api.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("users")
@SecurityRequirement(name = SECURITY_SCHEME)
class UsersController @Autowired constructor(
    private val service: UserService,
) {
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID) = service.getUser(id)
}
