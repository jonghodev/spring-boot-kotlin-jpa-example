package dev.jongho.springbootkotlinjpasample.domain.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserApi(private val userService: UserService) {

    @PostMapping("/signup")
    fun signup(@RequestBody dto: SignupDto) = userService.signup(dto.toUser())

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginDto) = userService.login(dto.email, dto.password)
}