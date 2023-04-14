package lab.maxb.dark_api.domain.service

import lab.maxb.dark_api.domain.model.RecognitionTask
import lab.maxb.dark_api.domain.model.ShortUserCredentials
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface TasksService {
    fun getAvailable(account: ShortUserCredentials, pageable: Pageable): List<RecognitionTask>
    fun add(task: RecognitionTask): RecognitionTask
    fun getTask(account: ShortUserCredentials, id: UUID): RecognitionTask
    fun solve(account: ShortUserCredentials, id: UUID, answer: String): Boolean
    fun mark(id: UUID, isAllowed: Boolean): Boolean
    fun uploadImage(account: ShortUserCredentials, id: UUID, file: MultipartFile): String
    fun downloadImage(path: String): ByteArrayResource
}
