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

        if (title.length > MAX_TITLE_LENGTH)
            addError("Title length exceeded (max $MAX_TITLE_LENGTH)")

        if (title.length < MIN_TITLE_LENGTH)
            addError("Title is too short (min $MIN_TITLE_LENGTH)")

        if (body.length < MIN_BODY_LENGTH)
            addError("Body is too short (min $MIN_BODY_LENGTH)")

        if (body.length > MAX_BODY_LENGTH)
            addError("Body length exceeded (max $MAX_BODY_LENGTH)")

        copy(title=title, body=body)
    }

    companion object {
        const val MAX_TITLE_LENGTH = 60
        const val MIN_TITLE_LENGTH = 3
        const val MIN_BODY_LENGTH = 3
        const val MAX_BODY_LENGTH = 5000
    }
}
