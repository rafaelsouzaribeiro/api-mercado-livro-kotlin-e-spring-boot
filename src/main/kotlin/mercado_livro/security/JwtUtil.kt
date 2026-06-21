package mercado_livro.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mercado_livro.expection.AuthenticationException
import org.springframework.beans.factory.annotation.Value
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

    fun isValidToken(token: String): Boolean {
        val claims= getClaims(token)
        return !(claims.expiration==null || claims.subject==null || Date().after(claims.expiration))
    }

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseSignedClaims(token)
                .payload
        }catch (e:Exception){
            throw AuthenticationException("Falha ao obter payload do token","000")
        }
    }

    fun getSubject(token: String):String {
        return getClaims(token).subject
    }
}