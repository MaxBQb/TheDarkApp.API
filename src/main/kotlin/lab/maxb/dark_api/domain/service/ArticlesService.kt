package lab.maxb.dark_api.domain.service

import lab.maxb.dark_api.domain.model.Article
import org.springframework.data.domain.Pageable
import java.util.*

interface ArticlesService {
    fun getAll(pageable: Pageable): List<Article>
    fun findById(id: UUID): Article
    fun add(model: Article): Article
    fun update(model: Article): Article
}
