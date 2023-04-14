package lab.maxb.dark_api.domain.service.implementation

import lab.maxb.dark_api.domain.gateway.UserCredentialsGateway
import lab.maxb.dark_api.infrastracture.configuration.security.Roles
import org.springframework.beans.factory.annotation.Autowired
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
                listOf(Roles.toAuthority(it.role))
            )
        }
}
