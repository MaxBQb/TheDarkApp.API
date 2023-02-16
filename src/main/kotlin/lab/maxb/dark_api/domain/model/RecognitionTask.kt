package lab.maxb.dark_api.domain.model

import java.util.*

data class RecognitionTask(
    val names: Set<String>,
    val images: List<String> = emptyList(),
    val owner: User,
    val solutions: Set<Solution> = emptySet(),
    val reviewed: Boolean = false,
    val id: UUID = randomUUID,
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}
