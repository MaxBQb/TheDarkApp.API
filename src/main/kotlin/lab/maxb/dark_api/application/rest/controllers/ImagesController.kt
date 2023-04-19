package lab.maxb.dark_api.application.rest.controllers

import com.google.common.io.ByteStreams
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.SECURITY_SCHEME
import lab.maxb.dark_api.domain.exceptions.UnknownError
import lab.maxb.dark_api.domain.service.ImagesService
import lab.maxb.dark_api.infrastracture.configuration.security.Roles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("images")
@SecurityRequirement(name = SECURITY_SCHEME)
class ImagesController @Autowired constructor(
    private val service: ImagesService,
) {
    @PostMapping("/", consumes = [MULTIPART_FORM_DATA_VALUE])
    @RolesAllowed(Roles.USER, Roles.PREMIUM_USER)
    fun uploadImage(@RequestParam file: MultipartFile)
        = try { service.save(file) } catch (e: IOException) {
            e.printStackTrace()
            throw UnknownError(e.message ?: "No extra details")
        }

    @GetMapping("/{id}")
    fun downloadImage(@PathVariable id: String): ResponseEntity<Resource> {
        val resource = service.get(id).use {
            ByteArrayResource(ByteStreams.toByteArray(it))
        }
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            add(HttpHeaders.PRAGMA, "no-cache")
            add(HttpHeaders.EXPIRES, "0")
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$id\"")
        }
        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource)
    }
}
