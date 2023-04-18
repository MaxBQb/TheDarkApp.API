package lab.maxb.dark_api.infrastracture.datasource.local

import lab.maxb.dark_api.infrastracture.datasource.local.model.RecognitionTaskLocalDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface RecognitionTaskDAO : JpaRepository<RecognitionTaskLocalDTO?, UUID?> {
    fun findByIdEquals(id: UUID): RecognitionTaskLocalDTO?

    @Query(value = """SELECT task FROM RecognitionTaskLocalDTO task
         WHERE task.owner.id <> ?1 AND task.reviewed = true
         AND NOT EXISTS(SELECT 1 FROM SolutionLocalDTO solution 
         WHERE solution.task.id = task.id AND solution.user.id = ?1) 
         GROUP BY task.id""")
    fun findTasksForUser(userId: UUID, pageable: Pageable): List<RecognitionTaskLocalDTO>

    @Query(value = """SELECT task FROM RecognitionTaskLocalDTO task
         WHERE task.id = ?1 AND task.owner.id <> ?2 AND task.reviewed = true
         AND NOT EXISTS(SELECT 1 FROM SolutionLocalDTO solution
         WHERE solution.task.id = ?1 AND solution.user.id = ?2)
         GROUP BY task.id
         """)
    fun findTaskForUser(id: UUID, userId: UUID): RecognitionTaskLocalDTO?
    fun findByOrderByReviewedAsc(pageable: Pageable): List<RecognitionTaskLocalDTO>
}
