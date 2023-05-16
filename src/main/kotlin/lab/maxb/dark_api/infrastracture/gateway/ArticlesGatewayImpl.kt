package lab.maxb.dark_api.infrastracture.gateway

import lab.maxb.dark_api.domain.gateway.ArticlesGateway
import lab.maxb.dark_api.domain.model.Article
import lab.maxb.dark_api.infrastracture.datasource.local.ArticleDAO
import lab.maxb.dark_api.infrastracture.datasource.local.model.toDomain
import lab.maxb.dark_api.infrastracture.datasource.local.model.toLocal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ArticlesGatewayImpl @Autowired constructor(
    private val dataSource: ArticleDAO
): ArticlesGateway {
    override fun save(model: Article)
        = dataSource.save(model.toLocal()).toDomain()

    override fun findById(id: UUID)
        = dataSource.findByIdOrNull(id)?.toDomain()

    override fun findAll(pageable: Pageable)
        = dataSource.findAll(pageable).toList().mapNotNull { it?.toDomain() }
}