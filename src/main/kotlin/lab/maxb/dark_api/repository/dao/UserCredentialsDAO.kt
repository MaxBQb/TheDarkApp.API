package lab.maxb.dark_api.repository.dao

import lab.maxb.dark_api.model.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserCredentialsDAO : JpaRepository<UserCredentials?, UUID?> {
    fun <T> findByLoginEquals(login: String, type: Class<T>): T?
    fun existsByLoginEquals(login: String): Boolean
}

inline fun<reified T> UserCredentialsDAO.findByLoginEquals(login: String)
    = findByLoginEquals(login, T::class.java)