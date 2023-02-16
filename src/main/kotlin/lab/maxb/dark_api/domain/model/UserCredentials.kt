package lab.maxb.dark_api.domain.model

import java.util.*

data class UserCredentials(
    val login: String,
    val password: String,
    val user: User,
    val role: Role = Role.USER,
    val id: UUID = randomUUID,
)

data class ShortUserCredentials(
    val user: User,
    val role: Role = Role.USER,
)
