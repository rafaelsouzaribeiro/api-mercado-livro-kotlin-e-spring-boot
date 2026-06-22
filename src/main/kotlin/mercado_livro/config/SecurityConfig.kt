package mercado_livro.config

import mercado_livro.enums.Roles
import mercado_livro.repository.CustomerRepository
import mercado_livro.security.AuthenticationFilter
import mercado_livro.security.AuthorizationFilter
import mercado_livro.security.CustomAuthenticationEntryPoint
import mercado_livro.security.JwtUtil
import mercado_livro.service.UserDetailCustomerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailCustomerService,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) {
    private val publicPostMatchers = arrayOf(
        "/customer"
    )

    private val adminMatchers = arrayOf(
        "/admin/**"
    )

    private val swaggerMatchers = arrayOf(
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**"
    )

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(userDetailsService: UserDetailsService): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider(userDetailsService)
        authProvider.setPasswordEncoder(bCryptPasswordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        authManager: AuthenticationManager
    ): SecurityFilterChain {
        http
            .cors(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(*swaggerMatchers).permitAll()
                auth.requestMatchers(HttpMethod.POST, *publicPostMatchers).permitAll()
                auth.requestMatchers(*adminMatchers).hasAuthority(Roles.ADMIN.description)
                auth.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilter(AuthenticationFilter(authManager, customerRepository,jwtUtil))
            .addFilter(AuthorizationFilter(authManager,userDetailsService,jwtUtil))
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
            }
        return http.build()
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowedOriginPatterns = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return CorsFilter(source)
    }
}
