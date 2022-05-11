package lab.maxb.dark_api.Model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DarkUser")
class User(
    @Column(nullable = false)
    var name: String = "Anonymous User",

    @Column(nullable = false)
    var rating: Int = 0,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID,
)