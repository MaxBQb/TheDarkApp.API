package lab.maxb.dark_api.domain.model

import java.util.*

data class Solution(
    val answer: String,
    val rating: Int = 1,
    val user: User,
    val taskId: UUID,
    val id: UUID = randomUUID,
)
