package lab.maxb.dark_api.infrastracture.gateway

import lab.maxb.dark_api.domain.gateway.UsersGateway
import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.infrastracture.datasource.local.UserDAO
import lab.maxb.dark_api.infrastracture.datasource.local.model.toDomain
import lab.maxb.dark_api.infrastracture.datasource.local.model.toLocal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UsersGatewayImpl @Autowired constructor(
    private val dataSource: UserDAO
): UsersGateway {
    override fun save(model: User)
        = dataSource.save(model.toLocal()).toDomain()

    override fun findById(id: UUID)
        = dataSource.findByIdEquals(id)?.toDomain()
}