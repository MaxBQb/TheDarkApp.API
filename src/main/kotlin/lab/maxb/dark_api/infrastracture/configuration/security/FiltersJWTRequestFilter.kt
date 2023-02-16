package lab.maxb.dark_api.infrastracture.configuration.security


import lab.maxb.dark_api.domain.service.implementation.JWTUtils
import lab.maxb.dark_api.domain.service.implementation.UserDetailsServiceImpl
import lab.maxb.dark_api.domain.service.implementation.getRoleFromAuthority
import lab.maxb.dark_api.domain.service.implementation.toAuthority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
                        if (role == getRoleFromAuthority(userDetails.authorities.first().authority))
                            userDetails.authorities
                        else listOf(role.toAuthority())
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