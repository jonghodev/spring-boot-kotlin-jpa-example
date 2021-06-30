package dev.jongho.springbootkotlinjpasample.global.security

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtProvider.resolveToken(request)

        /**
         * 토큰이 없다면 Skip
         */
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        val isValidToken = jwtProvider.verifyToken(token!!)

        if(!isValidToken) {
            response.status = HttpStatus.FORBIDDEN.value()
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.flushBuffer()
            return
        }

        filterChain.doFilter(request, response)
        return
    }
}