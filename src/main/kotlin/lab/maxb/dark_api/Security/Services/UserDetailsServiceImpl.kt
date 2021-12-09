package lab.maxb.dark_api.Security.Services

import lab.maxb.dark_api.DB.DAO.UserCredentialsDAO
import lab.maxb.dark_api.Model.UserCredentials
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    @Autowired
    private val userCredentialsDAO: UserCredentialsDAO
) : UserDetailsService {
    override fun loadUserByUsername(userName: String)
        = userCredentialsDAO.findByLoginEquals(userName,
          UserCredentials::class.java)?.let {
            User(
                it.login,
                it.password,
                listOf(SimpleGrantedAuthority(it.role.toString()))
            )
        }
}