package mercado_livro.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    private val publicPostMatchers = arrayOf(
        "/customer"
    )
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder{
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(HttpMethod.POST, *publicPostMatchers).permitAll()
                auth.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }
}