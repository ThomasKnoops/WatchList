package org.thoteman.watchlist.model

data class Review(
    val userId: String,
    val movieId: Int,
    val reviewScore: Int,
    val reviewText: String
)
