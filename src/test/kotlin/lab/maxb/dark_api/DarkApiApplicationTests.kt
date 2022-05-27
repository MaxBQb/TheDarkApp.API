package lab.maxb.dark_api

import lab.maxb.dark_api.model.UserCredentials
import lab.maxb.dark_api.model.pojo.AuthRequest
import lab.maxb.dark_api.repository.DatabaseFiller
import lab.maxb.dark_api.services.AuthService
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
        assertEquals(authService.login(AuthRequest(
            fillerConfig.admin.login,
            fillerConfig.admin.password,
        ))?.role, UserCredentials.Role.ADMINISTRATOR)
    }

}
