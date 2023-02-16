package lab.maxb.dark_api.domain.gateway

import lab.maxb.dark_api.domain.model.UserCredentials


interface UserCredentialsGateway : PersistGateway<UserCredentials> {
    fun findByLogin(login: String): UserCredentials?
    fun existsByLogin(login: String) = findByLogin(login) != null
}