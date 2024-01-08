package org.thoteman.watchlist.model

data class MovieReview(
    var userId: String = "",
    var movieId: Int = 0,
    var score: Int = 0,
    var reviewText: String = ""
)

