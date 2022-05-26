package lab.maxb.dark_api.repository.dao

import lab.maxb.dark_api.model.RecognitionTask
import lab.maxb.dark_api.model.pojo.RecognitionTaskListView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RecognitionTaskDAO : JpaRepository<RecognitionTask?, UUID?> {
    fun <T> findByIdEquals(id: UUID, type: Class<T>): T?
    fun <T> findByIdEqualsAndReviewedTrueAndOwner_IdNotAndSolutions_User_IdNotOrSolutionsNull(id: UUID, ownerId: UUID, userId: UUID, type: Class<T>): T?
    fun findByIdEqualsAndOwner_IdEquals(id: UUID, owner_id: UUID): RecognitionTask?
    fun findByOrderByReviewedAsc(pageable: Pageable): List<RecognitionTaskListView>
    fun findByReviewedTrueAndOwner_IdNotAndSolutions_User_IdNotOrSolutionsNull(ownerId: UUID, userId: UUID, pageable: Pageable): List<RecognitionTaskListView>
}

inline fun <reified T> RecognitionTaskDAO.findByIdEquals(id: UUID): T? = findByIdEquals(id, T::class.java)

fun RecognitionTaskDAO.findTasksForUser(id: UUID, pageable: Pageable)
    = findByReviewedTrueAndOwner_IdNotAndSolutions_User_IdNotOrSolutionsNull(id, id, pageable)

inline fun <reified T> RecognitionTaskDAO.findTaskForUser(id: UUID, userId: UUID)
    = findByIdEqualsAndReviewedTrueAndOwner_IdNotAndSolutions_User_IdNotOrSolutionsNull(id, userId, userId, T::class.java)
