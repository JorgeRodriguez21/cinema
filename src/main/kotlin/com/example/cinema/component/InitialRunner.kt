package com.example.cinema.component

import com.example.cinema.dto.MovieRest
import com.example.cinema.persistence.model.Movie
import com.example.cinema.persistence.model.Role
import com.example.cinema.persistence.model.User
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.UserRepository
import com.example.cinema.service.MovieRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class InitialRunner @Autowired constructor(
    private val movieRestService: MovieRestService,
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        createUser("user", Role.USER)
        createUser("admin", Role.ADMIN)
        movieRestService.loadOrders().forEach(::saveMovieToDatabase)
    }

    private fun saveMovieToDatabase(movieRest: MovieRest) {
        val foundMovie = movieRepository.findByImdbId(movieRest.id)
        if (foundMovie == null) {
            movieRepository.save(Movie(null, movieRest.title, movieRest.id))
        }
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