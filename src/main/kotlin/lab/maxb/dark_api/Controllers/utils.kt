package lab.maxb.dark_api.Controllers

import lab.maxb.dark_api.Security.Services.getRoleFromAuthority
import org.springframework.security.core.Authentication

val Authentication.role
    get() = getRoleFromAuthority(
        authorities.first().authority
    )