package lab.maxb.dark_api.services

import lab.maxb.dark_api.model.User
import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.model.pojo.AuthRequest
import lab.maxb.dark_api.model.pojo.AuthResponse
import java.util.*

interface AuthService {
    fun login(request: AuthRequest): AuthResponse?
    fun signup(request: AuthRequest): AuthResponse?
    fun setRole(login: String, role: UserCredentials.Role): Boolean
    fun getUserId(login: String): UUID?
    fun getUser(login: String): User?
}
