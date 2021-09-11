package com.example.cinema.api.controller

import com.example.cinema.CinemaApplication
import com.example.cinema.api.request.MovieReviewRequest
import com.example.cinema.security.SpringSecurityConfig
import com.example.cinema.service.persistence.ReviewService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
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

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SpringSecurityConfig::class, CinemaApplication::class])
@WebMvcTest(ReviewController::class)
@ActiveProfiles("test")
internal class ReviewControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var service: ReviewService

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["USER"])
    fun `should schedule a movie show with a price and a time for a given room`() {
        val request = MovieReviewRequest(1, "good movie", 3, "")
        val requestToCallTheService = MovieReviewRequest(1, "good movie", 3, "user1")
        justRun { service.addReviewForAMovie(requestToCallTheService) }

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/review").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        perform.andExpect(MockMvcResultMatchers.status().`is`(201))
    }
}