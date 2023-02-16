package lab.maxb.dark_api.application.response

import lab.maxb.dark_api.domain.model.RecognitionTask
import java.util.*

data class RecognitionTaskNetworkDTO(
    val names: Set<String>,
    val images: List<String>,
    val ownerId: UUID,
    val reviewed: Boolean,
    val id: UUID,
)

fun RecognitionTask.toNetworkDTO() = RecognitionTaskNetworkDTO(
    names = names,
    images = images,
    ownerId = owner.id,
    reviewed = reviewed,
    id = id,
)
