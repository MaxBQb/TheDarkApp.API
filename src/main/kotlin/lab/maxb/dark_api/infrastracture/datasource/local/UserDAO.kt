package lab.maxb.dark_api.infrastracture.datasource.local

import lab.maxb.dark_api.infrastracture.datasource.local.model.UserLocalDTO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserDAO : JpaRepository<UserLocalDTO, UUID> {
    fun findByIdEquals(id: UUID): UserLocalDTO?
}