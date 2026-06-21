package mercado_livro.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private var expiration: Long = 0

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(id: String): String {

        return Jwts.builder()
            .subject(id)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()
    }
}