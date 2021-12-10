package lab.maxb.dark_api.Model

import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.persistence.*

@Entity
class UserCredentials(
    @Column(nullable = false)
    var login: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    @OneToOne(optional = false, cascade = [CascadeType.ALL])
    var user: User? = null,

    @Column(nullable = false)
    var role: Role = Role.USER,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = getUUID()
) {
    enum class Role {
        ADMINISTRATOR,
        MODERATOR,
        CONSULTOR,
        PREMIUM_USER,
        USER,
    }
}

interface UserCredentialsView {
    @Value("#{target.user.id}")
    fun getUserId(): UUID
    fun getRole(): UserCredentials.Role
}

val UserCredentials.Role.isUser get() = when(this) {
    UserCredentials.Role.USER,
    UserCredentials.Role.PREMIUM_USER -> true
    else -> false
}

val UserCredentials.Role.hasControlPrivileges get() = when(this) {
    UserCredentials.Role.MODERATOR,
    UserCredentials.Role.ADMINISTRATOR -> true
    else -> false
}