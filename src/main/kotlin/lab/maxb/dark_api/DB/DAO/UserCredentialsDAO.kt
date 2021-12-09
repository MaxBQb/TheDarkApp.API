package lab.maxb.dark_api.DB.DAO

import lab.maxb.dark_api.Model.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserCredentialsDAO : JpaRepository<UserCredentials?, UUID?> {
    fun <T> findByLoginEquals(login: String?, type: Class<T>): T?
    fun existsByLoginEquals(login: String): Boolean
}