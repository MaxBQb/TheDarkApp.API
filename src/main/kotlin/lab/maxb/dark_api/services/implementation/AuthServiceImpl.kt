package lab.maxb.dark_api.services.implementation

import lab.maxb.dark_api.controllers.AuthController
import lab.maxb.dark_api.model.User
import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.model.UserCredentialsView
import lab.maxb.dark_api.model.pojo.AuthRequest
import lab.maxb.dark_api.model.pojo.AuthResponse
import lab.maxb.dark_api.repository.dao.UserCredentialsDAO
import lab.maxb.dark_api.repository.dao.findByLoginEquals
import lab.maxb.dark_api.services.AuthService
import lab.maxb.dark_api.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl @Autowired constructor(
    private val authenticationManager: AuthenticationManager,
    private var passwordEncoder: PasswordEncoder,
    private val jwtUtils: JWTUtils,
    private val dataSource: UserCredentialsDAO,
    private val userDetailsService: UserDetailsServiceImpl,
    private val userService: UserService,
) : AuthService {
    override fun login(request: AuthRequest): AuthResponse? {
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

    override fun signup(request: AuthRequest): AuthResponse? {
        if (dataSource.existsByLoginEquals(request.login))
            return null

        dataSource.save(
            UserCredentials(
                request.login,
                passwordEncoder.encode(request.password),
                userService.save(User(request.login)),
                UserCredentials.Role.USER
            )
        )

        return login(request)
    }

    override fun setRole(login: String, role: UserCredentials.Role)
        = dataSource.findByLoginEquals<UserCredentials>(login)?.let {
            it.role = role
            dataSource.save(it)
            true
        } ?: false

    private fun getAuthResponse(login: String)
            = dataSource.findByLoginEquals<UserCredentialsView>(login)?.let { credentials ->
        userDetailsService.loadUserByUsername(login)?.let {
            AuthResponse(
                jwtUtils.generateToken(it),
                credentials.user_id,
                credentials.role,
            )
        }
    }

    override fun getUserId(login: String)
        = dataSource.findByLoginEquals<UserCredentialsView>(login)?.user_id

    override fun getUser(login: String)
        = getUserId(login)?.let { userService.get(it) }
}
