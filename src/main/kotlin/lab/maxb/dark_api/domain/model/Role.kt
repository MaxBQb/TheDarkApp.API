package lab.maxb.dark_api.domain.model

enum class Role {
    ADMINISTRATOR,
    MODERATOR,
    CONSULTER,
    PREMIUM_USER,
    USER,
}

val Role.isUser get() = when(this) {
    Role.USER,
    Role.PREMIUM_USER -> true
    else -> false
}

val Role.hasControlPrivileges get() = when(this) {
    Role.MODERATOR,
    Role.ADMINISTRATOR -> true
    else -> false
}