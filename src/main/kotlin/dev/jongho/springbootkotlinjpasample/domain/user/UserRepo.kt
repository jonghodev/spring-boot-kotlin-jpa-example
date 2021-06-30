package dev.jongho.springbootkotlinjpasample.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepo : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}