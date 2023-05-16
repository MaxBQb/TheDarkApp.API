package lab.maxb.dark_api.application.rest.controllers

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lab.maxb.dark_api.SECURITY_SCHEME
import lab.maxb.dark_api.application.request.ArticleNetworkCreationDTO
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.application.response.ArticleNetworkDTO
import lab.maxb.dark_api.application.response.toNetworkDTO
import lab.maxb.dark_api.domain.service.ArticlesService
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.infrastracture.configuration.security.Roles
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("articles")
@SecurityRequirement(name = SECURITY_SCHEME)
class ArticlesController @Autowired constructor(
    private val service: ArticlesService,
    private val authService: AuthService,
) {
    @GetMapping("/")
    @PageableAsQueryParam
    fun getAllArticles(
        @Parameter(hidden = true) pageable: Pageable
    ) = service.getAll(pageable).map { it.toNetworkDTO() }

    @PostMapping("/")
    @RolesAllowed(Roles.MODERATOR, Roles.CONSULTER)
    fun addArticle(
        auth: Authentication,
        @RequestBody article: ArticleNetworkCreationDTO
    ): ArticleNetworkDTO {
        val credentials = authService.extractCredentials(auth)
        return service.add(article.toDomain(credentials.user)).toNetworkDTO()
    }

    @PutMapping("/{id}")
    @RolesAllowed(Roles.MODERATOR, Roles.CONSULTER)
    fun updateArticle(
        auth: Authentication,
        @PathVariable id: UUID,
        @RequestBody article: ArticleNetworkCreationDTO
    ): ArticleNetworkDTO {
        val credentials = authService.extractCredentials(auth)
        return service.update(article.toDomain(credentials.user, id)).toNetworkDTO()
    }
}
