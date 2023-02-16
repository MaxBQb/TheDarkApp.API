package lab.maxb.dark_api.domain.service.implementation

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import lab.maxb.dark_api.domain.model.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class JWTUtils @Autowired constructor(
    private val jwtConfig: JWTConfig,
) {
    fun extractUsername(token: String): String = extractClaim(token) {
        obj: Claims -> obj.subject
    }

    fun extractExpiration(token: String): Date = extractClaim(token) {
        obj: Claims -> obj.expiration
    }

    fun extractRole(token: String): Role
        = Role.valueOf(extractAllClaims(token).get(
            ROLE_KEY,
            String::class.java
        ))

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T)
        = claimsResolver(extractAllClaims(token))

    private fun extractAllClaims(token: String)
        = Jwts.parser().setSigningKey(jwtConfig.secret).parseClaimsJws(token).body

    private fun isTokenExpired(token: String)
        = extractExpiration(token).before(Date())

    fun generateToken(userDetails: UserDetails): String = createToken(
        hashMapOf(ROLE_KEY to getRoleFromAuthority(userDetails.authorities.first().authority).name),
        userDetails.username
    )

    private fun createToken(claims: Map<String, Any>, subject: String)
        = Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtConfig.duration.toMillis()))
            .signWith(SignatureAlgorithm.HS256, jwtConfig.secret)
            .compact()

    fun validateToken(token: String, userDetails: UserDetails)
        = extractUsername(token).let {
            it == userDetails.username && !isTokenExpired(token)
        }

    @ConfigurationProperties(prefix = "jwt")
    @ConstructorBinding
    data class JWTConfig(
        val secret: String,
        val duration: Duration,
    )

    companion object {
        private const val ROLE_KEY = "role"
    }
}