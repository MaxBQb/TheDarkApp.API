package lab.maxb.dark_api.application.response

import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.service.AuthService
import java.util.*

data class AuthResponse(
    val token: String,
    val id: UUID,
    val role: Role,
)

fun AuthService.AuthResponse.toNetwork() = AuthResponse(
    token = token,
    id = user.id,
    role = role,
)