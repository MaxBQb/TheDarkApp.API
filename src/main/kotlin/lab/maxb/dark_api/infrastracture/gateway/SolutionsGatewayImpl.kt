package lab.maxb.dark_api.infrastracture.gateway

import lab.maxb.dark_api.domain.gateway.SolutionsGateway
import lab.maxb.dark_api.domain.model.Solution
import lab.maxb.dark_api.infrastracture.datasource.local.SolutionDAO
import lab.maxb.dark_api.infrastracture.datasource.local.model.toDomain
import lab.maxb.dark_api.infrastracture.datasource.local.model.toLocal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SolutionsGatewayImpl @Autowired constructor(
    private val dataSource: SolutionDAO
) : SolutionsGateway {
    override fun existsByTaskIdAndUserId(id: UUID, userId: UUID)
        = dataSource.existsByTask_IdEqualsAndUser_IdEquals(id, userId)

    override fun save(model: Solution)
        = dataSource.save(model.toLocal()).toDomain()

    override fun findById(id: UUID)
        = dataSource.findByIdOrNull(id)?.toDomain()
}