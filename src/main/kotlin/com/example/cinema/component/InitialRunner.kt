package com.example.cinema.component

import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.persistence.model.Movie
import com.example.cinema.persistence.model.Role
import com.example.cinema.persistence.model.User
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.UserRepository
import com.example.cinema.service.rest.MovieRestService
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
        movieRestService.loadMovies().forEach(::saveMovieToDatabase)
    }

    private fun saveMovieToDatabase(movieRestDto: MovieRestDto) {
        val foundMovie = movieRepository.findByImdbId(movieRestDto.id)
        if (foundMovie == null) {
            movieRepository.save(Movie(null, movieRestDto.title, movieRestDto.id))
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