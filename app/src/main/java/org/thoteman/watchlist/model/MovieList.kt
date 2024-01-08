package org.thoteman.watchlist.model

data class MovieList(
    val page: Int,
    val results: List<MovieInfo>,
    val totalPages: Int,
    val totalResults: Int
)
