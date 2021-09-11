package com.example.cinema.api.request


data class MovieReviewRequest(val movieId: Int, val comment: String, val rate: Int, var username: String = "") {
    init {
        if (rate < 1 || rate > 5) {
            throw IllegalArgumentException("Rate can't be less than 1 nor more than 5")
        }
    }
}