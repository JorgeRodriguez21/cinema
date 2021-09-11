package com.example.cinema.api.controller

import com.example.cinema.CinemaApplication
import com.example.cinema.api.domain.DomainMovie
import com.example.cinema.api.request.MovieShowDeleteRequest
import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.api.response.MovieResponse
import com.example.cinema.persistence.model.RoomType
import com.example.cinema.security.SpringSecurityConfig
import com.example.cinema.service.MovieService
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
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
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SpringSecurityConfig::class, CinemaApplication::class])
@WebMvcTest(MovieController::class)
@ActiveProfiles("test")
internal class MovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var service: MovieService

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["ADMIN"])
    fun `should schedule a movie show with a price and a time for a given room`() {
        val request = MovieShowRequest(1, RoomType.NORMAL_3D, "15:30", BigDecimal.TEN)
        justRun { service.saveShowTimes(request) }

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/movie/show").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        perform.andExpect(MockMvcResultMatchers.status().`is`(201))
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["USER"])
    fun `should return 401 when the user has not the allowed role`() {
        val request = MovieShowRequest(1, RoomType.NORMAL_3D, "15:30", BigDecimal.TEN)

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/movie/show").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        perform.andExpect(MockMvcResultMatchers.status().`is`(401))
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["ADMIN"])
    fun `should return all the existent movies with the associated shows`() {
        val movie: DomainMovie = DomainMovie.fromModel(buildMovie())
        val expectedResponse = MovieResponse(listOf(movie))
        every { service.retrieveAllMoviesInformation() }.returns(listOf(movie))

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/movie/show")
        )

        perform.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedResponse)))
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["USER"])
    fun `should return 401 when the user has not the allowed role for getting movies shows`() {

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/movie/show")
        )

        perform.andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["ADMIN"])
    fun `should delete a movie show`() {
        val request = MovieShowDeleteRequest(1, 1)
        justRun { service.deleteMovieShow(request) }

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete("/movie/show").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        perform.andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["USER"])
    fun `should return 401 when the user has not the allowed role for delete movies show`() {

        val request = MovieShowDeleteRequest(1, 1)
        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete("/movie/show").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        perform.andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}
