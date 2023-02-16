package lab.maxb.dark_api.infrastracture.datasource.local

import lab.maxb.dark_api.infrastracture.datasource.local.model.SolutionLocalDTO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SolutionDAO : JpaRepository<SolutionLocalDTO?, UUID?> {
    fun existsByTask_IdEqualsAndUser_IdEquals(id: UUID, userId: UUID): Boolean
}
