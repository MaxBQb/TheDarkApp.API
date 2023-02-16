package lab.maxb.dark_api.domain.model

import java.util.*

data class User(
    val name: String = "Anonymous User",
    val rating: Int = 0,
    val id: UUID = randomUUID,
)