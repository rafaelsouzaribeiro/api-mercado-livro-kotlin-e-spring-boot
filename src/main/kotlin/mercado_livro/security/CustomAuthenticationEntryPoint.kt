package mercado_livro.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mercado_livro.controller.response.ErrorResponse
import mercado_livro.enums.Errors
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint{
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        response.contentType="application/json"
        response.status=HttpServletResponse.SC_UNAUTHORIZED
        var errorResponse=ErrorResponse(HttpStatus.UNAUTHORIZED.value(),Errors.ML_OOO.message,Errors.ML_OOO.code,null)
        response.outputStream.print(jacksonObjectMapper().writeValueAsString(errorResponse))
    }

}