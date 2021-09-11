package com.example.cinema.persistence.model

import javax.persistence.*

@Entity
@Table(name = "review")
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    val rate: Int,
    val comment: String,
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @ManyToOne()
    @JoinColumn(name = "movie_id", nullable = false)
    val movie: Movie
) {

    override fun toString(): String {
        return "Review(id=$id, rate=$rate, comment='$comment')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Review

        if (id != other.id) return false
        if (rate != other.rate) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + rate
        result = 31 * result + comment.hashCode()
        return result
    }
}