package lab.maxb.dark_api.DB.DAO

import lab.maxb.dark_api.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserDAO : JpaRepository<User, UUID> {

    fun findByIdEquals(id: UUID): User?

}