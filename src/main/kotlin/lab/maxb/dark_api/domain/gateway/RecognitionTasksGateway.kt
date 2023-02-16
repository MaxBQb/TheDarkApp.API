package lab.maxb.dark_api.domain.gateway

import lab.maxb.dark_api.domain.model.RecognitionTask
import org.springframework.data.domain.Pageable
import java.util.*

interface RecognitionTasksGateway : CRUDGateway<UUID, RecognitionTask> {
    fun findUserSpecific(userId: UUID, pageable: Pageable): List<RecognitionTask>
    fun findByIdUserSpecific(id: UUID, userId: UUID): RecognitionTask?
    fun findWithNotReviewedPrioritized(pageable: Pageable): List<RecognitionTask>
}