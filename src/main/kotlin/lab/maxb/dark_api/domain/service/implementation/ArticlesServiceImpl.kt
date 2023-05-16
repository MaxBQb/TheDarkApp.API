package lab.maxb.dark_api.domain.service.implementation

import lab.maxb.dark_api.domain.exceptions.NotFoundException
import lab.maxb.dark_api.domain.gateway.ArticlesGateway
import lab.maxb.dark_api.domain.model.Article
import lab.maxb.dark_api.domain.service.ArticlesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class ArticlesServiceImpl @Autowired constructor(
    private val gateway: ArticlesGateway,
) : ArticlesService {
    override fun getAll(pageable: Pageable)
        = gateway.findAll(pageable)

    override fun findById(id: UUID) = gateway.findById(id) ?: throw NotFoundException.of("Article")

    override fun add(model: Article)
        = gateway.save(model.validate())

    override fun update(model: Article)
        = if (gateway.existsById(model.id))
            gateway.save(model.validate())
        else throw NotFoundException.of("Article")
}
