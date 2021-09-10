package com.example.cinema.component

import com.example.cinema.dto.MovieRest
import com.example.cinema.persistence.model.Role
import com.example.cinema.persistence.model.User
import com.example.cinema.persistence.repository.UserRepository
import com.example.cinema.service.MovieRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class InitialRunner @Autowired constructor(
    private val movieRestService: MovieRestService,
    private val userRepository: UserRepository
) : CommandLineRunner {

    val list = mutableListOf<MovieRest>()

    override fun run(vararg args: String?) {
        createUser("user", Role.USER)
        createUser("admin", Role.ADMIN)
        movieRestService.loadOrders()
    }

    private fun createUser(username: String, role: Role) {
        if (userRepository.findByUsername(username) == null) {
            val user = User(
                null,
                username,
                BCryptPasswordEncoder().encode("password"),
                role
            )
            userRepository.save(user)
        }
    }
}