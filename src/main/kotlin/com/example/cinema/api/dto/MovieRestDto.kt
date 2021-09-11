package com.example.cinema.api.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieRestDto(
    @JsonAlias("imdbID") val id: String = "",
    @JsonAlias("Title") val title: String = "",
    @JsonAlias("Plot") val description: String = "",
    @JsonAlias("Released") val releaseDate: String,
    @JsonAlias("imdbRating") val imdbRating: Double,
    @JsonAlias("Runtime") val runtime: String = "",
    @JsonAlias("Ratings") val ratings: List<MovieRestRatingDto>,
    var reviewsOnCinema: Double = 0.0
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class MovieRestRatingDto(@JsonAlias("Source") val source: String = "", @JsonAlias("Value") val value: String = "")
