package com.example.cinema.api.response

import com.example.cinema.api.domain.DomainMovie

data class MovieResponse(val movies: List<DomainMovie>)