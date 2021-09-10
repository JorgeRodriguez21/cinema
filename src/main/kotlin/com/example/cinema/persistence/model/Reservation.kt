package com.example.cinema.persistence.model

import javax.persistence.*

@Entity
@Table(name = "reservation")
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "ticket_number")
    val ticketNumber: Int,
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @ManyToOne
    @JoinColumns(
        JoinColumn(name = "movie_id"),
        JoinColumn(name = "room_id")
    )
    val movieRoom: MovieRoom
)