package com.example.cinema.api.dto

import com.example.cinema.persistence.model.Review
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
    private var reviewsOnCinema: Double = 0.0,
    private var comments: List<String> = listOf()
) {
    fun getReviewsOnCinema(): Double {
        return reviewsOnCinema
    }

    fun getCommentsOnCinema(): List<String> {
        return comments
    }

    fun calculateReviewRate(reviews: List<Review>) {
        this.reviewsOnCinema = reviews.map { it.rate }.average()
    }

    fun setComments(reviews: List<Review>) {
        this.comments = reviews.map { it.comment }
    }
}

@JsonIgnoreProperties(ignoreUnknown = false)
data class MovieRestRatingDto(@JsonAlias("Source") val source: String = "", @JsonAlias("Value") val value: String = "")
