package org.thoteman.watchlist.model

data class MovieInfo(
    val id: Int,
    val title: String,
    val tagline: String,
    val overview: String,
    val popularity: Float,
    val release_date: String,
    val runtime: Int,
    val vote_average: Float,
    val vote_count: Int
)
