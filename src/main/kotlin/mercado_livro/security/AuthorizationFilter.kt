package mercado_livro.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mercado_livro.expection.AuthenticationException
import mercado_livro.service.UserDetailCustomerService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class AuthorizationFilter(
    private val authenticationManager: AuthenticationManager,
    private val userDetailCustomerService: UserDetailCustomerService,
    private val jwtUtil: JwtUtil
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authorization = request.getHeader("Authorization")

        if (authorization != null && authorization.startsWith("Bearer ")) {
            val token = getAuthentication(authorization.split(" ")[1])
            SecurityContextHolder.getContext().authentication=token
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        if (!jwtUtil.isValidToken(token)){
            throw AuthenticationException("token invalido","000")
        }
        val subject = jwtUtil.getSubject(token)
        val customer = userDetailCustomerService.loadUserByUsername(subject)
        return UsernamePasswordAuthenticationToken(customer,null,customer.authorities)

    }
}
