package lab.maxb.dark_api.infrastracture.gateway

import lab.maxb.dark_api.domain.gateway.UserCredentialsGateway
import lab.maxb.dark_api.domain.model.UserCredentials
import lab.maxb.dark_api.infrastracture.datasource.local.UserCredentialsDAO
import lab.maxb.dark_api.infrastracture.datasource.local.model.toDomain
import lab.maxb.dark_api.infrastracture.datasource.local.model.toLocal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class UserCredentialsGatewayImpl @Autowired constructor(
    private val dataSource: UserCredentialsDAO
): UserCredentialsGateway {
    override fun save(model: UserCredentials)
        = dataSource.save(model.toLocal()).toDomain()

    override fun findByLogin(login: String)
        = dataSource.findByLoginEquals(login)?.toDomain()
}
