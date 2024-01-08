package org.thoteman.watchlist.model

data class MovieInfo(
    val id: Int = 0,
    val title: String = "",
    val tagline: String = "",
    val overview: String = "",
    val popularity: Float = 0.toFloat(),
    val release_date: String = "",
    val runtime: Int = 0,
    val vote_average: Float = 0.toFloat(),
    val vote_count: Int = 0
)
