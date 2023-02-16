package lab.maxb.dark_api.application.rest.controllers

import lab.maxb.dark_api.application.request.AuthRequest
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.application.response.AuthResponse
import lab.maxb.dark_api.application.response.toNetwork
import lab.maxb.dark_api.domain.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
        = wrapResponse(service.login(body.toDomain()))

    @PostMapping("signup")
    fun signup(@RequestBody body: AuthRequest)
        = wrapResponse(service.signup(body.toDomain()))

    private fun wrapResponse(response: AuthService.AuthResponse?):
            ResponseEntity<AuthResponse> = response?.toNetwork()?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.badRequest().build()
}