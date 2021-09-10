package com.example.cinema.persistence.model

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "movie_room")
data class MovieRoom(
    @EmbeddedId
    val id: MovieRoomKey,
    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    val movie: Movie,
    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    val room: Room,
    val price: BigDecimal,
    @Column(name = "\"time\"")
    val time: String
)