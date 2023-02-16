package lab.maxb.dark_api.domain.service.implementation

import lab.maxb.dark_api.domain.gateway.UserCredentialsGateway
import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.model.ShortUserCredentials
import lab.maxb.dark_api.domain.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl @Autowired constructor(
    private val userCredentialsGateway: UserCredentialsGateway
) : UserDetailsService {
    override fun loadUserByUsername(userName: String)
        = userCredentialsGateway.findByLogin(userName)?.let {
            User(
                it.login,
                it.password,
                listOf(it.role.toAuthority())
            )
        }
}

fun Role.toAuthority()
    = SimpleGrantedAuthority("ROLE_"+toString())

fun AuthService.extractCredentials(auth: Authentication)
    = ShortUserCredentials(
        user = getUser(auth.name)!!,
        role = auth.role
    )

fun getRoleFromAuthority(authority: String)
    = Role.valueOf(authority.removePrefix("ROLE_"))

val Authentication.role
    get() = getRoleFromAuthority(
        authorities.first().authority
    )