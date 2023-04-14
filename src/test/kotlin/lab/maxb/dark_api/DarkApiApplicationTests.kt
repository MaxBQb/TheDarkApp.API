package lab.maxb.dark_api

import lab.maxb.dark_api.application.request.AuthRequest
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.domain.model.Role
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.infrastracture.configuration.db.DatabaseFiller
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DarkApiApplicationTests @Autowired constructor(
    private val authService: AuthService,
    private val fillerConfig: DatabaseFiller.Properties,
) {

    @Test
    fun hasAdmin() {
        assertEquals(authService.login(
            AuthRequest(
                fillerConfig.admin.login,
                fillerConfig.admin.password,
            ).toDomain()
        ).role, Role.ADMINISTRATOR)
    }

}
