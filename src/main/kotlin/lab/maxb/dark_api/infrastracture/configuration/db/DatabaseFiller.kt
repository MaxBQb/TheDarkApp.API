package lab.maxb.dark_api.infrastracture.configuration.db

import lab.maxb.dark_api.application.request.AuthRequest
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.domain.exceptions.AuthException
import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component


@Component
class DatabaseFiller @Autowired constructor(
    private val userService: UserService,
    private val authService: AuthService,
    private val properties: Properties,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        properties.moderator?.let {
            userService.addUser(User(
                "ModeratorZou",
                id=getOrCreateAccount(AuthRequest(
                    it.login,
                    it.password,
                ))
            ))
            authService.setRole(it.login, Role.MODERATOR)
        }

        userService.addUser(User(
            "Admin",
            id=getOrCreateAccount(AuthRequest(
                properties.admin.login,
                properties.admin.password,
            ))
        ))
        authService.setRole(properties.admin.login, Role.ADMINISTRATOR)
    }

    private fun getOrCreateAccount(auth: AuthRequest)
        = (try {
            authService.signup(auth.toDomain())
        } catch (e: AuthException.AlreadyExists) {
            authService.login(auth.toDomain())
        }).user.id

    @ConfigurationProperties(prefix = "database.prefill")
    @ConstructorBinding
    data class Properties(
        val admin: Credentials,
        val moderator: Credentials?
    ) {
        data class Credentials(
            val login: String,
            val password: String,
        )
    }
}