package lab.maxb.dark_api.DB.DAO

import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.RecognitionTaskDTOView
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RecognitionTaskDAO : JpaRepository<RecognitionTask?, UUID?> {
    fun findByIdEquals(id: UUID): RecognitionTask?
    fun findByOrderByReviewedAsc(): List<RecognitionTaskDTOView>
    fun findByReviewedEquals(reviewed: Boolean): List<RecognitionTaskDTOView>
}