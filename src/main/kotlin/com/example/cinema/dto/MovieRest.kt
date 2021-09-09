package com.example.cinema.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieRest(
    @JsonAlias("imdbID") val id: String = "",
    @JsonAlias("Title") val title: String = "",
    @JsonAlias("Plot") val description: String = "",
    @JsonAlias("Released") val releaseDate: String,
    @JsonAlias("imdbRating") val imdbRating: Double,
    @JsonAlias("Runtime") val runtime: String = "",
    @JsonAlias("Ratings") val ratings: List<MovieRestRating>
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class MovieRestRating(@JsonAlias("Source") val source: String = "", @JsonAlias("Value") val value: String = "")
