package dev.jongho.springbootkotlinjpasample.domain.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignupDto(@get:Email val email: String, @get:NotBlank val password: String, @get:NotBlank val name: String) {
    fun toUser() = User(this.email, this.name, this.password)
}

data class LoginDto(@get:Email val email: String, @get:NotBlank val password: String)