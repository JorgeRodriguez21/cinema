package com.example.cinema.api.dto

import com.example.cinema.persistence.model.RoomType
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class MovieDtoTest {

    @Test
    fun `should create a dto object from a model`() {
        val movie = buildMovie()
        val expectedDtoShow = MovieShowDto(1, RoomType.VIP_3D, "15:30", BigDecimal.TEN)
        val expectedDtoShow2 = MovieShowDto(2, RoomType.NORMAL, "16:30", BigDecimal.ONE)
        val expectedMovieDto = MovieDto(1, "movie", listOf(expectedDtoShow, expectedDtoShow2))

        val actualMovieDto = MovieDto.fromModel(movie)

        assertThat(expectedMovieDto).usingRecursiveComparison().isEqualTo(actualMovieDto)
    }


}