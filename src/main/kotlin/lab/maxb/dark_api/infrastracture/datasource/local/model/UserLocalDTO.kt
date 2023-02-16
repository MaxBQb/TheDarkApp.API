package lab.maxb.dark_api.infrastracture.datasource.local.model

import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.domain.model.randomUUID
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "dark_user")
class UserLocalDTO(
    @Column(nullable = false)
    var name: String = "Anonymous User",

    @Column(nullable = false)
    var rating: Int = 0,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID,
)

fun User.toLocal() = UserLocalDTO(
    name = name,
    rating = rating,
    id = id,
)

fun UserLocalDTO.toDomain() = User(
    name = name,
    rating = rating,
    id = id,
)