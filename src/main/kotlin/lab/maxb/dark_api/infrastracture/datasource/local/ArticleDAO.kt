package lab.maxb.dark_api.infrastracture.datasource.local

import lab.maxb.dark_api.infrastracture.datasource.local.model.ArticleLocalDTO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ArticleDAO : JpaRepository<ArticleLocalDTO?, UUID?>
