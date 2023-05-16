package lab.maxb.dark_api.application.request

import lab.maxb.dark_api.domain.model.Article
import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.domain.model.randomUUID
import java.util.*


class ArticleNetworkCreationDTO(
    var title: String,
    var body: String,
)

fun ArticleNetworkCreationDTO.toDomain(
    author: User,
    id: UUID? = null,
) = Article(
    title = title,
    body = body,
    author = author,
    id = id ?: randomUUID,
)