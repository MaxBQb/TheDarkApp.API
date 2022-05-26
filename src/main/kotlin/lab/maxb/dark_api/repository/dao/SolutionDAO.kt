package lab.maxb.dark_api.repository.dao

import lab.maxb.dark_api.model.Solution
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SolutionDAO : JpaRepository<Solution?, UUID?>
