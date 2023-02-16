package lab.maxb.dark_api.infrastracture.datasource.local.model

import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.model.UserCredentials
import lab.maxb.dark_api.domain.model.randomUUID
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_credentials")
class UserCredentialsLocalDTO(
    @Column(nullable = false)
    var login: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    @OneToOne(optional = false, cascade = [CascadeType.ALL])
    var user: UserLocalDTO? = null,

    @Column(nullable = false)
    var role: Role = Role.USER,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID
)

fun UserCredentials.toLocal() = UserCredentialsLocalDTO(
    login = login,
    password = password,
    user = user.toLocal(),
    role = role,
    id = id,
)

fun UserCredentialsLocalDTO.toDomain() = UserCredentials(
    login = login!!,
    password = password!!,
    user = user!!.toDomain(),
    role = role,
    id = id,
)