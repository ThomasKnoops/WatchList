package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieInfoViewModelFactory(private val movieId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieInfoViewModel(movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
