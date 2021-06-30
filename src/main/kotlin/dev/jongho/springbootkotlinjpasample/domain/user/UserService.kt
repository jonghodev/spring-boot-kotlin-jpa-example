package dev.jongho.springbootkotlinjpasample.domain.user

import dev.jongho.springbootkotlinjpasample.domain.event.UserSignupEvent
import dev.jongho.springbootkotlinjpasample.global.exception.BadRequestException
import dev.jongho.springbootkotlinjpasample.global.exception.EntityNotFoundException
import dev.jongho.springbootkotlinjpasample.global.exception.ForbiddenException
import dev.jongho.springbootkotlinjpasample.global.security.JwtProvider
import org.mindrot.jbcrypt.BCrypt
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val publisher: ApplicationEventPublisher
    ) {

    /**
     * Signup
     */
    fun signup(user: User): User = user
        .validateEmailForSignup()
        .encryptPassword()
        .save()
        .publishEvent()

    private fun User.validateEmailForSignup(): User = userRepo
        .findByEmail(this.email)
        .ifPresent { throw BadRequestException("This email is already taken.") }
        .let { this }

    private fun User.encryptPassword(): User = this
        .let { password = passwordEncoder.encode(password); this }

    private fun User.save(): User = userRepo
        .save(this)

    private fun User.publishEvent(): User = this
        .also { publisher.publishEvent(UserSignupEvent(email, "User signup mail contents!")) }

    /**
     * Login
     */
    fun login(email: String, password: String): String = validateEmailForLogin(email)
        .comparePassword(password)
        .let { jwtProvider.generateToken(it.id!!) }

    private fun validateEmailForLogin(email: String): User = userRepo
        .findByEmail(email)
        .orElseThrow{ throw EntityNotFoundException("This email isn't registered.") }

    private fun User.comparePassword(rawPassword: String): User {
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw ForbiddenException("Wrong password.")
        }
        return this
    }

}