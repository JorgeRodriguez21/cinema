package com.example.cinema.api.domain

import com.example.cinema.persistence.model.RoomType
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class DomainMovieTest {

    @Test
    fun `should create a domain object from a model`() {
        val movie = buildMovie()
        val expectedDomainShow = DomainMovieShow(1, RoomType.VIP_3D, "15:30", BigDecimal.TEN)
        val expectedDomainShow2 = DomainMovieShow(2, RoomType.NORMAL, "16:30", BigDecimal.ONE)
        val expectedMovieDomain = DomainMovie(1, "movie", listOf(expectedDomainShow, expectedDomainShow2))

        val actualMovieDomain = DomainMovie.fromModel(movie)

        assertThat(expectedMovieDomain).usingRecursiveComparison().isEqualTo(actualMovieDomain)
    }


}