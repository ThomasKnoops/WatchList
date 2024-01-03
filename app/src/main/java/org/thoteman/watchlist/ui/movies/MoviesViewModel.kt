package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.thoteman.watchlist.BuildConfig

class MoviesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = BuildConfig.TMDB_API_KEY
    }
    val text: LiveData<String> = _text
}