package com.example.cinema.api.controller

import com.example.cinema.CinemaApplication
import com.example.cinema.api.dto.ReservationDto
import com.example.cinema.api.request.MovieReservationRequest
import com.example.cinema.api.request.MovieReviewRequest
import com.example.cinema.persistence.model.RoomType
import com.example.cinema.security.SpringSecurityConfig
import com.example.cinema.service.persistence.ReservationService
import com.example.cinema.service.persistence.ReviewService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.Assertions.*
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
@WebMvcTest(ReservationController::class)
@ActiveProfiles("test")
internal class ReservationControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var service: ReservationService

    @Test
    @WithMockUser(username = "user1", password = "pwd", authorities = ["USER"])
    fun `should schedule a movie show with a price and a time for a given room`() {
        val request = MovieReservationRequest(1, 1, 3, "")
        val requestToCallTheService = MovieReservationRequest(1, 1, 3, "user1")
        val expectedResponse = ReservationDto("movie", 3, RoomType.NORMAL, BigDecimal.TEN)
        every { service.addReservation(requestToCallTheService) }.returns(expectedResponse)

        val perform: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/reservation").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        perform.andExpect(MockMvcResultMatchers.status().`is`(201))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedResponse)))
    }
}