package org.thoteman.watchlist.model

data class MovieReview(
    val userId: String,
    val movieId: Int,
    val score: Int,
    val reviewText: String
)
