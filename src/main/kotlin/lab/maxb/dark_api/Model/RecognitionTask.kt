package lab.maxb.dark_api.Model

import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.persistence.*

@Entity
class RecognitionTask(
    @ElementCollection
    var names: Set<String>? = null,

    @ElementCollection
    var images: List<String>? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var owner: User? = null,

    @Column(nullable = false)
    var reviewed: Boolean = false,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = getUUID()
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}

interface RecognitionTaskDTOView {
    fun getNames(): Set<String>?
    @Value("#{target.owner.id}")
    fun getOwnerId(): UUID?
    fun getImages(): List<String>?
    fun getReviewed(): Boolean
    fun getId(): UUID
}

class RecognitionTaskDTOCreation(
    var names: Set<String>? = null,
    var images: List<String>? = null,
    var owner_id: UUID? = null,
) {
    fun toRecognitionTask(owner: User)
        = RecognitionTask(names, images, owner)
}
