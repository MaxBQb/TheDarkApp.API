package lab.maxb.dark_api.infrastracture.datasource.local.model

import lab.maxb.dark_api.domain.model.Article
import lab.maxb.dark_api.domain.model.randomUUID
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "article")
class ArticleLocalDTO(
    @Column(nullable = false)
    var title: String = "Untitled",

    @Column(nullable = false)
    var body: String = "No content",

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var author: UserLocalDTO? = null,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID
)

fun Article.toLocal() = ArticleLocalDTO(
    title = title,
    body = body,
    author = author.toLocal(),
    id = id,
)

fun ArticleLocalDTO.toDomain() = Article(
    title = title,
    body = body,
    author = author!!.toDomain(),
    id = id,
)