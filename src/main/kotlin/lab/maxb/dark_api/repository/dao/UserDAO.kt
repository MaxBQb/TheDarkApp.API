package lab.maxb.dark_api.repository.dao

import lab.maxb.dark_api.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserDAO : JpaRepository<User, UUID> {
    fun findByIdEquals(id: UUID): User?
}