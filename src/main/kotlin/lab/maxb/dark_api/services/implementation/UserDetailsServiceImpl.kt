package lab.maxb.dark_api.services.implementation

import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.repository.dao.UserCredentialsDAO
import lab.maxb.dark_api.repository.dao.findByLoginEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl @Autowired constructor(
    private val dataSource: UserCredentialsDAO
) : UserDetailsService {
    override fun loadUserByUsername(userName: String)
        = dataSource.findByLoginEquals<UserCredentials>(userName)?.let {
            User(
                it.login,
                it.password,
                listOf(it.role.toAuthority())
            )
        }
}

fun UserCredentials.Role.toAuthority()
    = SimpleGrantedAuthority("ROLE_"+toString())

fun getRoleFromAuthority(authority: String)
    = UserCredentials.Role.valueOf(authority.removePrefix("ROLE_"))