package lab.maxb.dark_api.domain.gateway

import lab.maxb.dark_api.domain.model.Article
import org.springframework.data.domain.Pageable
import java.util.*

interface ArticlesGateway : ReadWriteGateway<UUID, Article> {
    fun findAll(pageable: Pageable): List<Article>
}