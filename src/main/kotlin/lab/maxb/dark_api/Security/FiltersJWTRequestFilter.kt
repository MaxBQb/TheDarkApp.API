package lab.maxb.dark_api.Security


import lab.maxb.dark_api.Model.UserCredentials
import lab.maxb.dark_api.Security.Services.JWTUtils
import lab.maxb.dark_api.Security.Services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class FiltersJWTRequestFilter @Autowired constructor(
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtUtils: JWTUtils,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        parseJwt(request)?.let { jwt ->
            val userName = jwtUtils.extractUsername(jwt)
            val role = jwtUtils.extractRole(jwt)
            SecurityContextHolder.getContext().authentication?.run { return@let }
            userDetailsService.loadUserByUsername(userName)?.let { userDetails ->
                if (jwtUtils.validateToken(jwt, userDetails)) {
                    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        if (role == UserCredentials.Role.valueOf(userDetails.authorities.first().authority))
                            userDetails.authorities
                        else listOf(SimpleGrantedAuthority(role.name))
                    ).also {
                        it.details = WebAuthenticationDetailsSource().buildDetails(request)
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        val prefix = "Bearer "
        return if (!headerAuth.isNullOrBlank() && headerAuth.startsWith(prefix)) {
            headerAuth.substring(prefix.length, headerAuth.length)
        } else null
    }
}