package lab.maxb.dark_api.domain.service

import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.domain.model.UserCredentials
import java.util.*

interface AuthService {
    fun login(request: UserCredentials): AuthResponse?
    fun signup(request: UserCredentials): AuthResponse?

    fun setRole(login: String, role: Role): Boolean
    fun getUserId(login: String): UUID?
    fun getUser(login: String): User?

    data class AuthResponse(
        var token: String,
        var user: User,
        var role: Role,
    )
}
