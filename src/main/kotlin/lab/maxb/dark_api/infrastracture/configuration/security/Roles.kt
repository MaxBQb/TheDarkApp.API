package lab.maxb.dark_api.infrastracture.configuration.security

import lab.maxb.dark_api.domain.model.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority

object Roles {
    fun fromAuthority(authority: String) = Role.valueOf(authority.removePrefix(PREFIX))
    fun toAuthority(role: Role) = SimpleGrantedAuthority("$PREFIX$role")
    fun parseAuthority(authority: String) = toAuthority(fromAuthority(authority))

    private const val PREFIX = "ROLE_"

    /** String const for [Role.USER] */
    const val USER = PREFIX + "USER"

    /** String const for [Role.PREMIUM_USER] */
    const val PREMIUM_USER = PREFIX + "PREMIUM_USER"

    /** String const for [Role.MODERATOR] */
    const val MODERATOR = PREFIX + "MODERATOR"

    /** String const for [Role.ADMINISTRATOR] */
    const val ADMINISTRATOR = PREFIX + "ADMINISTRATOR"

    /** String const for [Role.CONSULTER] */
    const val CONSULTER = PREFIX + "CONSULTER"
}