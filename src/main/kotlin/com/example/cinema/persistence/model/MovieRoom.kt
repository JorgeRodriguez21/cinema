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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieRoom

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "MovieRoom(id=$id, price=$price, time='$time')"
    }


}