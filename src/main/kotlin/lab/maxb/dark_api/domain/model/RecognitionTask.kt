package lab.maxb.dark_api.domain.model

import lab.maxb.dark_api.domain.exceptions.applyValidation
import java.util.*

data class RecognitionTask(
    val names: Set<String>,
    val images: List<String> = emptyList(),
    val owner: User,
    val reviewed: Boolean = false,
    val id: UUID = randomUUID,
) {

    fun validate() = applyValidation {
        if (images.count() > MAX_IMAGES_COUNT)
            addError("Images amount exceeded (${MAX_IMAGES_COUNT})")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecognitionTask

        if (names != other.names) return false
        // List comparison somehow works differently (maybe because of Java origin of List (mapping from DB))
        if (images.zip(other.images).any { it.first != it.second }) return false
        if (owner != other.owner) return false
        if (reviewed != other.reviewed) return false
        return id == other.id
    }

    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}
