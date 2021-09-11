package com.example.cinema.api.controller

import com.example.cinema.CinemaApplication
import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.security.SpringSecurityConfig
import com.example.cinema.service.application.MovieApplicationService
import com.example.cinema.service.rest.MovieRestService
import com.example.cinema.utils.TestMovieBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SpringSecurityConfig::class, CinemaApplication::class])
@WebMvcTest(MovieRestController::class)
@ActiveProfiles("test")
internal class MovieRestControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var service: MovieApplicationService

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["USER"])
    fun `should return all the existent movies with the associated shows`() {
        val id = 1
        val movie = TestMovieBuilder.buildMovie()
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        movieRestDto.setComments(movie.reviews.toList())
        movieRestDto.calculateReviewRate(movie.reviews.toList())
        every { service.getMovieDetails(id) }.returns(movieRestDto)

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/movie/${id}")
        )

        perform.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(movieRestDto)))
    }
}