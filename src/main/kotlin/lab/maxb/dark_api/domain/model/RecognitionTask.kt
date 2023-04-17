package lab.maxb.dark_api.domain.model

import lab.maxb.dark_api.domain.exceptions.applyValidation
import java.util.*

data class RecognitionTask(
    val names: Set<String>,
    val images: List<String> = emptyList(),
    val owner: User,
    val solutions: Set<Solution> = emptySet(),
    val reviewed: Boolean = false,
    val id: UUID = randomUUID,
) {

    fun validate() = applyValidation {
        if (images.count() > MAX_IMAGES_COUNT)
            addError("Images amount exceeded (${MAX_IMAGES_COUNT})")
    }

    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}
