package lab.maxb.dark_api.DB.DAO

import lab.maxb.dark_api.Model.POJO.RecognitionTaskFullView
import lab.maxb.dark_api.Model.POJO.RecognitionTaskListView
import lab.maxb.dark_api.Model.RecognitionTask
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RecognitionTaskDAO : JpaRepository<RecognitionTask?, UUID?> {
    fun findByIdEqualsAndReviewedTrue(id: UUID): RecognitionTaskFullView?
    fun <T> findByIdEquals(id: UUID, type: Class<T>): T?
    fun findByOrderByReviewedAsc(): List<RecognitionTaskListView>
    fun findByReviewedTrueAndOwnerIdNot(ownerId: UUID): List<RecognitionTaskListView>
}