package lab.maxb.dark_api.Controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.DB.DAO.RecognitionTaskDAO
import lab.maxb.dark_api.DB.DAO.UserCredentialsDAO
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.DB.DAO.findByIdEquals
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskFullView
import lab.maxb.dark_api.Model.POJO.RecognitionTaskImages
import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.RecognitionTask.Companion.MAX_IMAGES_COUNT
import lab.maxb.dark_api.Model.UserCredentialsView
import lab.maxb.dark_api.Model.hasControlPrivileges
import lab.maxb.dark_api.Model.isUser
import lab.maxb.dark_api.SECURITY_SCHEME
import lab.maxb.dark_api.services.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*


@RestController
@RequestMapping("task")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@SecurityRequirement(name = SECURITY_SCHEME)
class RecognitionTaskController @Autowired constructor(
    private val recognitionTaskDAO: RecognitionTaskDAO,
    private val userDAO: UserDAO,
    private val userCredentialsDAO: UserCredentialsDAO,
    private val imageService: ImageService
) {

    @GetMapping("/all")
    fun getAllRecognitionTasks(auth: Authentication)
        = if (auth.role.isUser)
            recognitionTaskDAO.findByReviewedTrueAndOwnerIdNot(
                getUserId(auth)
            )
        else if (auth.role.hasControlPrivileges)
            recognitionTaskDAO.findByOrderByReviewedAsc()
        else null

    @GetMapping("/{id}")
    fun getRecognitionTask(auth: Authentication,
                           @PathVariable id: UUID):
            RecognitionTaskFullView?
        = if (auth.role.isUser)
            recognitionTaskDAO.findByIdEqualsAndReviewedTrue(id)
        else if (auth.role.hasControlPrivileges)
            recognitionTaskDAO.findByIdEquals<RecognitionTaskFullView>(id)
        else null

    @PatchMapping("mark/{id}/{isAllowed}")
    fun markRecognitionTask(auth: Authentication,
                            @PathVariable id: UUID,
                            @PathVariable isAllowed: Boolean): Boolean {
        if (!auth.role.hasControlPrivileges)
            return false
        return recognitionTaskDAO.findByIdEquals(id, RecognitionTask::class.java)?.let {
            if (!isAllowed && !it.reviewed) {
                recognitionTaskDAO.findByIdEquals<RecognitionTaskImages>(id)?.images?.forEach { path ->
                    runCatching { imageService.delete(path) }
                }
                recognitionTaskDAO.deleteById(id)
            } else {
                it.reviewed = isAllowed
                recognitionTaskDAO.save(it)
            }
            true
        } ?: false
    }

    @PostMapping("{id}/image", consumes = [MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam file: MultipartFile
    ): String? {
        val task = recognitionTaskDAO.findByIdEqualsAndOwner_IdEquals(
            id, getUserId(auth)
        ) ?: return null
        if (task.images!!.count() >= MAX_IMAGES_COUNT)
            return null

        return try {
            val fileName = imageService.save(file)
            task.images!!.add(fileName)
            recognitionTaskDAO.save(task)
            fileName
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @GetMapping("/image/{path}")
    fun downloadImage(@PathVariable path: String): ResponseEntity<Resource> {
        val file = imageService.get(path) ?: return ResponseEntity.noContent().build()
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            add(HttpHeaders.PRAGMA, "no-cache")
            add(HttpHeaders.EXPIRES, "0")
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$path\"")
        }
        val resource = ByteArrayResource(file.readAllBytes())
        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource)
    }

    @PostMapping("/add")
    fun addRecognitionTask(
        auth: Authentication,
        @RequestBody task: RecognitionTaskCreationDTO
    ) = userDAO.findByIdEquals(getUserId(auth))?.let {
            recognitionTaskDAO.save(RecognitionTask(
                task.names,
                mutableListOf(),
                it
            )).id
        }

    @GetMapping("/solve/{id}")
    fun solveRecognitionTask(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam answer: String
    ): Boolean? = userDAO.findByIdEquals(getUserId(auth))?.let { user ->
        recognitionTaskDAO.findByIdEquals<RecognitionTask>(id)?.let { task ->
            if (task.owner!!.id == user.id || !task.reviewed)
                null
            else if (answer in task.names!!) {
                recognitionTaskDAO.deleteById(task.id)
                true
            } else false
        }
    }

    private fun getUserId(auth: Authentication)
        = userCredentialsDAO.findByLoginEquals(auth.name,
            UserCredentialsView::class.java
        )!!.user_id
}
