package org.thoteman.watchlist.model

data class Movie(
    val id: Int,
    val title: String,
    val vote_average: Float,
    val vote_count: Int
)