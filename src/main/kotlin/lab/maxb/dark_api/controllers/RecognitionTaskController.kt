package lab.maxb.dark_api.controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.SECURITY_SCHEME
import lab.maxb.dark_api.model.pojo.RecognitionTaskCreationDTO
import lab.maxb.dark_api.services.TasksService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("task")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@SecurityRequirement(name = SECURITY_SCHEME)
class RecognitionTaskController @Autowired constructor(
    private val service: TasksService,
) {

    @GetMapping("/all")
    fun getAllRecognitionTasks(
        auth: Authentication,
        pageable: Pageable
    ) = service.getAvailable(auth, pageable)

    @GetMapping("/{id}")
    fun getRecognitionTask(
        auth: Authentication,
        @PathVariable id: UUID
    ) = service.get(auth, id)

    @PatchMapping("mark/{id}/{isAllowed}")
    fun markRecognitionTask(
        auth: Authentication,
        @PathVariable id: UUID,
        @PathVariable isAllowed: Boolean
    ) = service.mark(auth, id, isAllowed)

    @PostMapping("{id}/image", consumes = [MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam file: MultipartFile
    ) = service.uploadImage(auth, id, file)

    @GetMapping("/image/{path}")
    fun downloadImage(@PathVariable path: String): ResponseEntity<Resource> {
        val resource = service.downloadImage(path) ?: return ResponseEntity.noContent().build()
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            add(HttpHeaders.PRAGMA, "no-cache")
            add(HttpHeaders.EXPIRES, "0")
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$path\"")
        }
        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource)
    }

    @PostMapping("/add")
    fun addRecognitionTask(
        auth: Authentication,
        @RequestBody task: RecognitionTaskCreationDTO
    ) = service.add(auth, task)

    @GetMapping("/solve/{id}")
    fun solveRecognitionTask(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam answer: String
    ) = service.solve(auth, id, answer)
}
