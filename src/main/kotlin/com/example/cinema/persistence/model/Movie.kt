package com.example.cinema.persistence.model

import javax.persistence.*

@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    val title: String,
    @Column(name = "imdb_id", unique = true)
    val imdbId: String,
    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private val reviews: Set<Review> = setOf(),
    @OneToMany(mappedBy = "movie")
    val moviesRooms: Set<MovieRoom> = setOf()
)