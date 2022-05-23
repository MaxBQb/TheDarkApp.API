package lab.maxb.dark_api.repository

import lab.maxb.dark_api.model.User
import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.model.pojo.AuthRequest
import lab.maxb.dark_api.services.AuthService
import lab.maxb.dark_api.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class DatabaseFiller @Autowired constructor(
    private val userService: UserService,
    private val authService: AuthService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        userService.save(User(
            "Max",
            12,
            getOrCreateAccount(AuthRequest(
                "User",
                "123"
            ))!!.id
        ))

        userService.save(User(
            "Yet Another User",
            id=getOrCreateAccount(AuthRequest(
                "User2",
                "123",
            ))!!.id
        ))

        userService.save(User(
            "ModeratorZou",
            id=getOrCreateAccount(AuthRequest(
                "Moderator",
                "321",
            ))!!.id
        ))
        userService.save(User(
            "Admin",
            id=getOrCreateAccount(AuthRequest(
                "Admin",
                "111"
            ))!!.id
        ))
        
        authService.setRole("Moderator", UserCredentials.Role.MODERATOR)
        authService.setRole("Admin", UserCredentials.Role.ADMINISTRATOR)
    }

    private fun getOrCreateAccount(auth: AuthRequest)
        = authService.signup(auth) ?: authService.login(auth)
}