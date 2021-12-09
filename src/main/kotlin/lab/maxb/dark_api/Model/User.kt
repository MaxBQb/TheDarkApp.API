package lab.maxb.dark_api.Model

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
    @Column(nullable = false)
    var name: String = "Anonymous User",

    @Column(nullable = false)
    var rating: Int = 0,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = getUUID(),
)