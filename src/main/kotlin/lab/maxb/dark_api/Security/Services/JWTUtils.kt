package lab.maxb.dark_api.Security.Services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import lab.maxb.dark_api.Model.UserCredentials
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTUtils {
    fun extractUsername(token: String): String = extractClaim(token) {
        obj: Claims -> obj.subject
    }

    fun extractExpiration(token: String): Date = extractClaim(token) {
        obj: Claims -> obj.expiration
    }

    fun extractRole(token: String): UserCredentials.Role
        = UserCredentials.Role.valueOf(extractAllClaims(token).get(
            ROLE_KEY,
            String::class.java
        ))

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T)
        = claimsResolver(extractAllClaims(token))

    private fun extractAllClaims(token: String)
        = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body

    private fun isTokenExpired(token: String)
        = extractExpiration(token).before(Date())

    fun generateToken(userDetails: UserDetails): String = createToken(
        hashMapOf(ROLE_KEY to userDetails.authorities.first().authority),
        userDetails.username
    )

    private fun createToken(claims: Map<String, Any>, subject: String)
        = Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenDuration))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()

    fun validateToken(token: String, userDetails: UserDetails)
        = extractUsername(token).let {
            it == userDetails.username && !isTokenExpired(token)
        }

    companion object {
        const val tokenDuration = 1000 * 60 * 60 * 24
        private const val ROLE_KEY = "role"
        private const val SECRET_KEY = "Dak-tron-gSere-tcgh-f7e7-8hg-f78-6fj-t789-e4j0-4few-89d56-789-4ed5-fj8e3-0w"
        // TODO: Re-Generate and hide SECRET_KEY
    }
}