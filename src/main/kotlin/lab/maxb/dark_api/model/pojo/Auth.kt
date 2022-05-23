package lab.maxb.dark_api.model.pojo

import lab.maxb.dark_api.model.UserCredentials
import java.util.*

class AuthRequest(
    var login: String,
    var password: String,
)

class AuthResponse(
    var token: String,
    var id: UUID,
    var role: UserCredentials.Role,
)