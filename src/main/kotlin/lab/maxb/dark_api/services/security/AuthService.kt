package lab.maxb.dark_api.services.security

import lab.maxb.dark_api.controllers.AuthController
import lab.maxb.dark_api.repository.dao.UserCredentialsDAO
import lab.maxb.dark_api.repository.dao.UserDAO
import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.model.UserCredentialsView
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService @Autowired constructor(
    private val authenticationManager: AuthenticationManager,
    private var passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtUtils: JWTUtils,
    private val userCredentialsDAO: UserCredentialsDAO,
    private val userDAO: UserDAO,
) {
    fun login(request: AuthRequest): AuthResponse? {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.login,
                    request.password
                )
            )
        } catch (e: Exception) {
            if (e is BadCredentialsException)
                LoggerFactory.getLogger(AuthController::class.java)
                    .warn("Incorrect username or password")
            else
                e.printStackTrace()
            return null
        }
        return getAuthResponse(request.login)
    }

    fun signup(request: AuthRequest): AuthResponse? {
        if (userCredentialsDAO.existsByLoginEquals(request.login))
            return null

        userCredentialsDAO.save(UserCredentials(
            request.login,
            passwordEncoder.encode(request.password),
            userDAO.save(lab.maxb.dark_api.model.User()),
            UserCredentials.Role.USER
        ))

        return login(request)
    }

    private fun getAuthResponse(login: String)
            = userCredentialsDAO.findByLoginEquals(login,
        UserCredentialsView::class.java)?.let { credentials ->
        userDetailsService.loadUserByUsername(login)?.let {
            AuthResponse(
                jwtUtils.generateToken(it),
                credentials.user_id,
                credentials.role,
            )
        }
    }

    class AuthRequest(
        var login: String,
        var password: String,
    )

    class AuthResponse(
        var token: String,
        var id: UUID,
        var role: UserCredentials.Role,
    )
}