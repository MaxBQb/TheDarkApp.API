package lab.maxb.dark_api.repository

import lab.maxb.dark_api.model.User
import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.model.pojo.AuthRequest
import lab.maxb.dark_api.services.AuthService
import lab.maxb.dark_api.services.UserService
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
            userService.save(User(
                "ModeratorZou",
                id=getOrCreateAccount(AuthRequest(
                    it.login,
                    it.password,
                ))!!.id
            ))
            authService.setRole(it.login, UserCredentials.Role.MODERATOR)
        }

        userService.save(User(
            "Admin",
            id=getOrCreateAccount(AuthRequest(
                properties.admin.login,
                properties.admin.password,
            ))!!.id
        ))
        authService.setRole(properties.admin.login, UserCredentials.Role.ADMINISTRATOR)
    }

    private fun getOrCreateAccount(auth: AuthRequest)
        = authService.signup(auth) ?: authService.login(auth)

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