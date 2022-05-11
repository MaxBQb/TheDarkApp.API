package lab.maxb.dark_api

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

const val SECURITY_SCHEME = "Dark API Security"

@SpringBootApplication
@OpenAPIDefinition(info = Info(
    title = "Dark API",
    version = "1.0",
    description = "API for the DarkApp"))
@SecurityScheme(
    name = SECURITY_SCHEME,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@ConfigurationPropertiesScan
class DarkApiApplication

fun main(args: Array<String>) {
    runApplication<DarkApiApplication>(*args)
}
