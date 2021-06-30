package dev.jongho.springbootkotlinjpasample.domain.event

import dev.jongho.springbootkotlinjpasample.domain.mail.MailService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UserSignupHandler(private val mailService: MailService) {

    @Async
    @EventListener
    fun sendMail(event: UserSignupEvent) {
        mailService.sendMail(event.email, event.contents)
    }
}