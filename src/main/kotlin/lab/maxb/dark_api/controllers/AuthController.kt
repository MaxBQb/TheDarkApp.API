package lab.maxb.dark_api.controllers

import lab.maxb.dark_api.model.pojo.AuthRequest
import lab.maxb.dark_api.model.pojo.AuthResponse
import lab.maxb.dark_api.services.AuthService
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
        = wrapResponse(service.login(body))

    @PostMapping("signup")
    fun signup(@RequestBody body: AuthRequest)
        = wrapResponse(service.signup(body))

    private fun wrapResponse(response: AuthResponse?):
            ResponseEntity<AuthResponse> = response?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.badRequest().build()
}