package lab.maxb.dark_api.repository.dao

import lab.maxb.dark_api.model.pojo.RecognitionTaskFullView
import lab.maxb.dark_api.model.pojo.RecognitionTaskListView
import lab.maxb.dark_api.model.RecognitionTask
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RecognitionTaskDAO : JpaRepository<RecognitionTask?, UUID?> {
    fun findByIdEqualsAndReviewedTrue(id: UUID): RecognitionTaskFullView?
    fun <T> findByIdEquals(id: UUID, type: Class<T>): T?
    fun findByIdEqualsAndOwner_IdEquals(id: UUID, owner_id: UUID): RecognitionTask?
    fun findByOrderByReviewedAsc(pageable: Pageable): List<RecognitionTaskListView>
    fun findByReviewedTrueAndOwnerIdNot(ownerId: UUID): List<RecognitionTaskListView>
}

inline fun <reified T> RecognitionTaskDAO.findByIdEquals(id: UUID): T? = findByIdEquals(id, T::class.java)

