package lab.maxb.dark_api.domain.gateway

import lab.maxb.dark_api.domain.model.Solution
import java.util.*


interface SolutionsGateway : ReadWriteGateway<UUID, Solution> {
    fun existsByTaskIdAndUserId(id: UUID, userId: UUID): Boolean
}