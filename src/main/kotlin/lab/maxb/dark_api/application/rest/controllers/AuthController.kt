package lab.maxb.dark_api.application.rest.controllers

import lab.maxb.dark_api.application.request.AuthRequest
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.application.response.toNetwork
import lab.maxb.dark_api.domain.model.ShortUserCredentials
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.infrastracture.configuration.security.Roles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("auth")
class AuthController @Autowired constructor(
    private val service: AuthService,
) {
    @PostMapping("login")
    fun login(@RequestBody body: AuthRequest)
        = service.login(body.toDomain()).toNetwork()

    @PostMapping("signup")
    fun signup(@RequestBody body: AuthRequest)
        = service.signup(body.toDomain()).toNetwork()
}

fun AuthService.extractCredentials(auth: Authentication)
    = ShortUserCredentials(
        user = getUser(auth.name)!!,
        role = Roles.fromAuthority(auth.authorities.first().authority)
    )