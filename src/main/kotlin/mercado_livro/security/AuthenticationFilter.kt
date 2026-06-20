package mercado_livro.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mercado_livro.controller.request.LoginRequest
import mercado_livro.expection.AuthenticationException
import mercado_livro.repository.CustomerRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val customerRepository: CustomerRepository
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {

        return try {
            val auth = jacksonObjectMapper().readValue<LoginRequest>(request.inputStream)
            val id = customerRepository.findByEmail(auth.email)?.id
                ?: throw AuthenticationException("Usuário não encontrado", "99")
            val token =
                UsernamePasswordAuthenticationToken(id, auth.password)
            authenticationManager.authenticate(token)
        } catch (e: Exception) {
            throw AuthenticationException(
                "Falha ao autenticar",
                "99"
            )
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val id =(authResult.principal as UserCustomDetails).id

        response.addHeader("Authorization","999")
    }
}