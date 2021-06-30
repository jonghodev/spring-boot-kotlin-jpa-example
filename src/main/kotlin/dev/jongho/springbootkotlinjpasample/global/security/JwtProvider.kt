package dev.jongho.springbootkotlinjpasample.global.security

import dev.jongho.springbootkotlinjpasample.domain.user.UserRepo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Date
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

private const val TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L // one week

@Component
class JwtProvider(
    @Value(value = "\${jwt.secretKey}") val secretKey: String,
    private val userRepo: UserRepo
) {
    fun generateToken(userId: Long): String {
        val claims = Jwts.claims().setSubject(userId.toString())
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun verifyToken(jwtToken: String): Boolean {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
        val now = Date()
        return !claims.body.expiration.before(now)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }

    @Transactional
    fun getAuthentication(token: String): Authentication {
        val id = getUserIdFromToken(token)
        val user = userRepo.findById(id)

        return UsernamePasswordAuthenticationToken(user, "")
    }

    private fun getUserIdFromToken(token: String): Long {
        val id = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
        return id.toLong()
    }
}