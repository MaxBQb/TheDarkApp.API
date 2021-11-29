package lab.maxb.dark_api.Model

enum class Role {
    ADMINISTRATOR,
    MODERATOR,
    CONSULTOR,
    PREMIUM_USER,
    USER,
}

fun Role.isUser() = when(this) {
    Role.USER, Role.PREMIUM_USER -> true
    else -> false
}