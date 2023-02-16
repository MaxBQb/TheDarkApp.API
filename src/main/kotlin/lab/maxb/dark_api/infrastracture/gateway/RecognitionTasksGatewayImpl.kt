package lab.maxb.dark_api.infrastracture.gateway

import lab.maxb.dark_api.domain.gateway.RecognitionTasksGateway
import lab.maxb.dark_api.domain.model.RecognitionTask
import lab.maxb.dark_api.infrastracture.datasource.local.RecognitionTaskDAO
import lab.maxb.dark_api.infrastracture.datasource.local.model.toDomain
import lab.maxb.dark_api.infrastracture.datasource.local.model.toLocal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RecognitionTasksGatewayImpl @Autowired constructor(
    private val dataSource: RecognitionTaskDAO
): RecognitionTasksGateway {
    override fun findUserSpecific(userId: UUID, pageable: Pageable)
        = dataSource.findTasksForUser(userId, pageable).map { it.toDomain() }

    override fun findByIdUserSpecific(id: UUID, userId: UUID)
        = dataSource.findTaskForUser(id, userId)?.toDomain()

    override fun findWithNotReviewedPrioritized(pageable: Pageable)
        = dataSource.findByOrderByReviewedAsc(pageable).map { it.toDomain() }

    override fun save(model: RecognitionTask)
        = dataSource.save(model.toLocal()).toDomain()

    override fun findById(id: UUID)
        = dataSource.findByIdEquals(id)?.toDomain()

    override fun deleteById(id: UUID) = dataSource.deleteById(id)
}