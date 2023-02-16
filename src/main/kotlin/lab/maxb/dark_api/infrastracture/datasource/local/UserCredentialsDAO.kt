package lab.maxb.dark_api.infrastracture.datasource.local

import lab.maxb.dark_api.infrastracture.datasource.local.model.UserCredentialsLocalDTO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserCredentialsDAO : JpaRepository<UserCredentialsLocalDTO?, UUID?> {
    fun findByLoginEquals(login: String): UserCredentialsLocalDTO?
}
