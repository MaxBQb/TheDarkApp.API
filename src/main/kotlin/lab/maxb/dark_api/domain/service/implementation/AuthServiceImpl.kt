package lab.maxb.dark_api.domain.service.implementation

import lab.maxb.dark_api.application.rest.controllers.AuthController
import lab.maxb.dark_api.domain.exceptions.AuthException
import lab.maxb.dark_api.domain.gateway.UserCredentialsGateway
import lab.maxb.dark_api.domain.gateway.UsersGateway
import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.model.UserCredentials
import lab.maxb.dark_api.domain.service.AuthService
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
    private val credentialsGateway: UserCredentialsGateway,
    private val userDetailsService: UserDetailsServiceImpl,
    private val usersGateway: UsersGateway,
) : AuthService {
    override fun login(request: UserCredentials): AuthService.AuthResponse {
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
            throw AuthException.WrongCredentials()
        }
        return getAuthResponse(request.login)
    }

    override fun signup(request: UserCredentials): AuthService.AuthResponse {
        if (credentialsGateway.existsByLogin(request.login))
            throw AuthException.AlreadyExists()

        credentialsGateway.save(
            request.copy(
                password = passwordEncoder.encode(request.password),
                user = usersGateway.save(request.user),
            )
        )

        return login(request)
    }

    override fun setRole(login: String, role: Role)
        = credentialsGateway.findByLogin(login)?.let {
            credentialsGateway.save(it.copy(role = role))
            true
        } ?: false

    private fun getAuthResponse(login: String)
            = credentialsGateway.findByLogin(login)?.let { credentials ->
        userDetailsService.loadUserByUsername(login)?.let {
            AuthService.AuthResponse(
                jwtUtils.generateToken(it),
                credentials.user,
                credentials.role,
            )
        }
    } ?: throw AuthException.WrongCredentials()

    override fun getUserId(login: String)
        = credentialsGateway.findByLogin(login)?.user?.id

    override fun getUser(login: String)
        = getUserId(login)?.let { usersGateway.findById(it) }
}
