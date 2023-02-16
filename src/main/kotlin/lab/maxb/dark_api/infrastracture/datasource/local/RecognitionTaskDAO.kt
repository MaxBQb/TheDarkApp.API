package lab.maxb.dark_api.infrastracture.datasource.local

import lab.maxb.dark_api.infrastracture.datasource.local.model.RecognitionTaskLocalDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface RecognitionTaskDAO : JpaRepository<RecognitionTaskLocalDTO?, UUID?> {
    fun findByIdEquals(id: UUID): RecognitionTaskLocalDTO?

    @Query(value = """SELECT MIN(image.images) as image,
         CAST(task.owner_id as varchar) as owner_id,
         task.reviewed as reviewed, 
         CAST(task.id as varchar) as id FROM recognition_task task 
         JOIN recognition_task_images image on task.id = image.recognition_task_id 
         WHERE task.owner_id <> ?1 AND task.reviewed = true
         AND NOT EXISTS(SELECT 1 FROM solution s 
         WHERE s.task_id = task.id AND s.user_id = ?1) 
         GROUP BY task.id""", nativeQuery = true)
    fun findTasksForUser(userId: UUID, pageable: Pageable): List<RecognitionTaskLocalDTO>

    @Query(value = """SELECT ARRAY_AGG(DISTINCT answer.names) as names,
         ARRAY_AGG(DISTINCT image.images) as images,
         CAST(task.owner_id as varchar) as owner_id,
         task.reviewed as reviewed, 
         CAST(task.id as varchar) as id FROM recognition_task task 
         JOIN recognition_task_names answer on task.id = answer.recognition_task_id 
         JOIN recognition_task_images image on task.id = image.recognition_task_id 
         WHERE task.id = ?1 AND task.owner_id <> ?2 AND task.reviewed = true
         AND NOT EXISTS(SELECT 1 FROM solution s 
         WHERE s.task_id = ?1 AND s.user_id = ?2)
         GROUP BY task.id
         """, nativeQuery = true)
    fun findTaskForUser(id: UUID, userId: UUID): RecognitionTaskLocalDTO?
    fun findByOrderByReviewedAsc(pageable: Pageable): List<RecognitionTaskLocalDTO>
}
