package lab.maxb.dark_api.domain.model

import lab.maxb.dark_api.domain.exceptions.withValidation
import java.util.*

data class Article(
    val title: String,
    val body: String,
    val author: User,
    val id: UUID = randomUUID,
) {

    fun validate() = withValidation {
        val title = title.trim()
        val body = body.trim()
        if (title.length > MAX_TITLE_SIZE)
            addError("Title length exceeded (${MAX_TITLE_SIZE})")
        copy(title=title, body=body)
    }

    companion object {
        const val MAX_TITLE_SIZE = 60
    }
}
