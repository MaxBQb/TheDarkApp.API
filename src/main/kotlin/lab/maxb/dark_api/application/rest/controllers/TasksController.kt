package lab.maxb.dark_api.application.rest.controllers

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.SECURITY_SCHEME
import lab.maxb.dark_api.application.request.RecognitionTaskNetworkCreationDTO
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.application.response.RecognitionTaskNetworkDTO
import lab.maxb.dark_api.application.response.toNetworkDTO
import lab.maxb.dark_api.application.response.toNetworkListDTO
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.domain.service.TasksService
import lab.maxb.dark_api.infrastracture.configuration.security.Roles
import org.springdoc.core.converters.models.PageableAsQueryParam
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
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("tasks")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@SecurityRequirement(name = SECURITY_SCHEME)
class TasksController @Autowired constructor(
    private val service: TasksService,
    private val authService: AuthService,
) {
    @GetMapping("/")
    @PageableAsQueryParam
    fun getAllRecognitionTasks(
        auth: Authentication,
        @Parameter(hidden = true) pageable: Pageable
    ) = service.getAvailable(
        authService.extractCredentials(auth), pageable,
    ).map { it.toNetworkListDTO() }

    @GetMapping("/{id}")
    fun getRecognitionTask(
        auth: Authentication,
        @PathVariable id: UUID
    ) = service.getTask(
        authService.extractCredentials(auth),
        id,
    ).toNetworkDTO()

    @PostMapping("/{id}/approve/")
    @RolesAllowed(Roles.MODERATOR)
    fun markRecognitionTaskApproved(@PathVariable id: UUID) = service.mark(id, true)

    @PostMapping("/{id}/decline/")
    @RolesAllowed(Roles.MODERATOR)
    fun markRecognitionTaskDeclined(@PathVariable id: UUID) = service.mark(id, false)

    @PostMapping("{id}/images/", consumes = [MULTIPART_FORM_DATA_VALUE])
    @RolesAllowed(Roles.USER, Roles.PREMIUM_USER)
    fun uploadImage(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam file: MultipartFile
    ) = service.uploadImage(authService.extractCredentials(auth), id, file)

    @GetMapping("/image/{path}")
    fun downloadImage(@PathVariable path: String): ResponseEntity<Resource> {
        val resource = service.downloadImage(path)
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            add(HttpHeaders.PRAGMA, "no-cache")
            add(HttpHeaders.EXPIRES, "0")
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$path\"")
        }
        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource)
    }

    @PostMapping("/")
    @RolesAllowed(Roles.USER, Roles.PREMIUM_USER)
    fun addRecognitionTask(
        auth: Authentication,
        @RequestBody task: RecognitionTaskNetworkCreationDTO
    ): RecognitionTaskNetworkDTO? {
        val credentials = authService.extractCredentials(auth)
        task.owner = credentials.user
        return service.add(task.toDomain()).toNetworkDTO()
    }

    @PostMapping("{id}/solutions/")
    @RolesAllowed(Roles.USER, Roles.PREMIUM_USER)
    fun solveRecognitionTask(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestParam answer: String
    ) = service.solve(authService.extractCredentials(auth), id, answer)
}
