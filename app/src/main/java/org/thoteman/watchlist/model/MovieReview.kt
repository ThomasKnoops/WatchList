package org.thoteman.watchlist.model

data class MovieReview(
    var userId: String = "",
    var movieId: Int = 0,
    var movieTitle: String = "",
    var score: Int = 0,
    var reviewText: String = ""
)

