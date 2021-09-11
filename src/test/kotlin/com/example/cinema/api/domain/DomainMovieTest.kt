package com.example.cinema.api.domain

import com.example.cinema.persistence.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class DomainMovieTest {

    @Test
    fun `should create a domain object from a model`() {
        val movie = Movie(1, "movie", "1234")
        val room = Room(1, RoomType.VIP_3D)
        val room2 = Room(2, RoomType.NORMAL)
        val movieRoom = MovieRoom(MovieRoomKey(1, 1), movie, room, BigDecimal.TEN, "15:30")
        val movieRoom2 = MovieRoom(MovieRoomKey(1, 2), movie, room2, BigDecimal.ONE, "16:30")
        movie.moviesRooms.add(movieRoom)
        movie.moviesRooms.add(movieRoom2)
        val expectedDomainShow = DomainMovieShow(1, RoomType.VIP_3D, "15:30", BigDecimal.TEN)
        val expectedDomainShow2 = DomainMovieShow(2, RoomType.NORMAL, "16:30", BigDecimal.ONE)
        val expectedMovieDomain = DomainMovie(1, "movie", listOf(expectedDomainShow, expectedDomainShow2))

        val actualMovieDomain = DomainMovie.fromModel(movie)

        assertThat(expectedMovieDomain).usingRecursiveComparison().isEqualTo(actualMovieDomain)
    }

}