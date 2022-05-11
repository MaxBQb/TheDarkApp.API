package lab.maxb.dark_api.Model

import java.util.*
import javax.persistence.*

@Entity
class RecognitionTask(
    @ElementCollection
    var names: Set<String>? = null,

    @ElementCollection
    var images: MutableList<String>? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var owner: User? = null,

    @Column(nullable = false)
    var reviewed: Boolean = false,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}
