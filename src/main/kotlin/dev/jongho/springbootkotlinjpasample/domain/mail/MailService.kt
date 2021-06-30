package dev.jongho.springbootkotlinjpasample.domain.mail

import org.springframework.stereotype.Service

@Service
class MailService {
    fun sendMail(email: String, contents: String) = println("Mail sent to $email $contents")
}