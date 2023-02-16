package lab.maxb.dark_api.application.response

import lab.maxb.dark_api.domain.model.RecognitionTask
import java.util.*

data class RecognitionTaskNetworkListDTO(
    val image: String?,
    val ownerId: UUID,
    val reviewed: Boolean,
    val id: UUID,
)

fun RecognitionTask.toNetworkListDTO() = RecognitionTaskNetworkListDTO(
    image = images.firstOrNull(),
    ownerId = owner.id,
    reviewed = reviewed,
    id = id,
)