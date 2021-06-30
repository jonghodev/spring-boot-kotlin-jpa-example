package dev.jongho.springbootkotlinjpasample.domain.user

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
        @Column(unique = true)
        val email: String,

        val name: String,

        var password: String,

        @Id @GeneratedValue
        val id: Long? = null,
)