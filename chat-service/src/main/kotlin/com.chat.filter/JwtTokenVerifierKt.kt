package com.chat.filter

import com.chat.exception.CustomExceptionHandler.getExceptionResponse
import com.chat.interfaces.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Arrays
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun <T> Arrays.fromString(list: String): List<String> {
    return list.substring(1, list.length - 1).split(",")
}

class JwtTokenVerifierKt(private val jwtService: JwtService) :
    OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authentication = this.generateAuthentication(httpServletRequest = request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (ex: Exception) {
            getExceptionResponse(response, HttpStatus.UNAUTHORIZED)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun generateAuthentication(httpServletRequest: HttpServletRequest): Authentication {
        val username = httpServletRequest.getHeader("username")
        //val simpleGrantedAuthorities: List<SimpleGrantedAuthority> =
          Arrays.fromString(httpServletRequest.getHeader("authorities"))

        //isValidRole(username, simpleGrantedAuthorities)
        return UsernamePasswordAuthenticationToken(
            username,
            null,
            null//  simpleGrantedAuthorities
        )
    }

    private fun isValidRole(username: String, authorities: List<SimpleGrantedAuthority>) {
        this.jwtService.isValidRole(username, authorities)
    }
}