package com.example.cinema.persistence.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MovieRoomKey(
    @Column(name = "movie_id")
    var movieId: Int,
    @Column(name = "room_id")
    var roomId: Int
) : Serializable