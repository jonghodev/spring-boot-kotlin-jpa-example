package dev.jongho.springbootkotlinjpasample.domain.event

import javax.validation.constraints.Email

class UserSignupEvent(@Email val email: String, val contents: String)
