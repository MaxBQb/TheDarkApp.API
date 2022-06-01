@file:Suppress("unused")

package lab.maxb.dark_api.model.pojo

import org.springframework.beans.factory.annotation.Value
import java.util.*

interface RecognitionTaskListView {
    val image: String?
    val owner_id: UUID
    val reviewed: Boolean
    val id: UUID
}

@Suppress("SpringElInspection")
interface RecognitionTaskListViewAuto: RecognitionTaskListView {
    override val image: String? @Value("#{target.images[0]}") get
    override val owner_id: UUID @Value("#{target.owner.id}") get
}

interface RecognitionTaskFullView {
    val names: Set<String>
    val images: List<String>
    val owner_id: UUID
    val reviewed: Boolean
    val id: UUID
}

@Suppress("SpringElInspection")
interface RecognitionTaskFullViewAuto: RecognitionTaskFullView {
    override val owner_id: UUID @Value("#{target.owner.id}") get
}

interface RecognitionTaskImages {
    var images: List<String>?
}

class RecognitionTaskCreationDTO(
    var names: Set<String>
)