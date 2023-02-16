package lab.maxb.dark_api.application.request

import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.domain.model.UserCredentials

data class AuthRequest(
    var login: String,
    var password: String,
)

fun AuthRequest.toDomain() = UserCredentials(
    login = login,
    password = password,
    user = User(login)
)