package lab.maxb.dark_api.application.response

import lab.maxb.dark_api.domain.model.Article
import java.util.*

data class ArticleNetworkDTO(
    val title: String,
    val body: String,
    val authorId: UUID,
    val id: UUID,
)

fun Article.toNetworkDTO() = ArticleNetworkDTO(
    title = title,
    body = body,
    authorId = author.id,
    id = id,
)
