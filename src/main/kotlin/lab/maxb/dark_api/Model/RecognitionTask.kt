package lab.maxb.dark_api.Model

import lab.maxb.dark_api.Model.getUUID
import java.util.*

open class RecognitionTask(
    open var names: Set<String>? = null,
    open var images: List<String>? = null,
    open var owner: User? = null,
    open var reviewed: Boolean = false,
    open var id: UUID = getUUID(),
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}