package com.example.cinema.persistence.model

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "review")
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val rate: Int,
    val comment: String,
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @ManyToOne()
    @JoinColumn(name = "movie_id", nullable = false)
    val movie: Movie
)