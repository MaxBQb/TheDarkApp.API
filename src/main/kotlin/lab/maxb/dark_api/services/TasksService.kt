package lab.maxb.dark_api.services

import lab.maxb.dark_api.model.pojo.RecognitionTaskCreationDTO
import lab.maxb.dark_api.model.pojo.RecognitionTaskFullView
import lab.maxb.dark_api.model.pojo.RecognitionTaskListView
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface TasksService {
    fun getAvailable(auth: Authentication, pageable: Pageable): List<RecognitionTaskListView>?
    fun add(auth: Authentication, task: RecognitionTaskCreationDTO): UUID?
    fun get(auth: Authentication, id: UUID): RecognitionTaskFullView?
    fun solve(auth: Authentication, id: UUID, answer: String): Boolean?
    fun mark(auth: Authentication, id: UUID, isAllowed: Boolean): Boolean
    fun uploadImage(auth: Authentication, id: UUID, file: MultipartFile): String?
    fun downloadImage(path: String): ByteArrayResource?
}
