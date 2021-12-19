package lab.maxb.dark_api.Controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.DB.DAO.RecognitionTaskDAO
import lab.maxb.dark_api.DB.DAO.UserCredentialsDAO
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.*
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskFullView
import lab.maxb.dark_api.Model.RecognitionTask.Companion.MAX_IMAGES_COUNT
import lab.maxb.dark_api.SECURITY_SCHEME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("task")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@SecurityRequirement(name = SECURITY_SCHEME)
class RecognitionTaskController @Autowired constructor(
    private val recognitionTaskDAO: RecognitionTaskDAO,
    private val userDAO: UserDAO,
    private val userCredentialsDAO: UserCredentialsDAO,
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
            recognitionTaskDAO.findByIdEquals(id, RecognitionTaskFullView::class.java)
        else null

    @RolesAllowed("MODERATOR")
    @PatchMapping("mark/{id}")
    fun markRecognitionTask(@PathVariable id: UUID,
                            @RequestParam isAllowed: Boolean)
        = recognitionTaskDAO.findByIdEquals(id, RecognitionTask::class.java)?.let {
            if (!isAllowed && !it.reviewed)
                recognitionTaskDAO.deleteById(id)
            else {
                it.reviewed = isAllowed
                recognitionTaskDAO.save(it)
            }
            true
        } ?: false

    @PostMapping("{id}/image", consumes = [MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam file: MultipartFile
    ): UUID? {
        val task = recognitionTaskDAO.findByIdEqualsAndOwner_IdEquals(
            id, getUserId(auth)
        ) ?: return null
        if (task.images!!.count() >= MAX_IMAGES_COUNT)
            return null
        val id = getUUID()
        val fileName = id.toString()
        try {
            saveFile("images/", fileName, file)
            task.images!!.add(fileName)
            recognitionTaskDAO.save(task)
        } catch (e: IOException) { return null }
        return id
    }

    @GetMapping("/image/{path}")
    fun downloadImage(@PathVariable path: String): ResponseEntity<Resource?> {
        return try {
            val file = File("images/${StringUtils.cleanPath(path)}")
            val headers = HttpHeaders()
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate")
            headers.add("Pragma", "no-cache")
            headers.add("Expires", "0")

            val path = Paths.get(file.absolutePath)
            val resource = ByteArrayResource(Files.readAllBytes(path))
            ResponseEntity.ok().headers(headers).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource)
        } catch (e: java.nio.file.NoSuchFileException) {
            ResponseEntity.noContent().build()
        }
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
        recognitionTaskDAO.findByIdEquals(id, RecognitionTask::class.java)?.let { task ->
            if (task.owner!!.id == user.id || !task.reviewed)
                return null
            return if (answer in task.names!!) {
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
