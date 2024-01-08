package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thoteman.watchlist.BuildConfig
import java.io.IOException
import com.google.gson.Gson
import org.thoteman.watchlist.model.Movie
import org.thoteman.watchlist.model.MovieList

class MoviesViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>().apply {
        value = emptyList() // Initial value while the request is being made
    }
    val movies: LiveData<List<Movie>> = _movies

    private val client = OkHttpClient()

    init {
        loadMovies()
    }

    fun loadMovies() {
        // Launch a coroutine to perform the network request asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            val allMovies = mutableListOf<Movie>()

            for (i in 1..5) {
                try {
                    val request = Request.Builder()
                        .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=$i&sort_by=popularity.desc")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                        .build()

                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val gson = Gson()
                        val movies = gson.fromJson(responseBody, MovieList::class.java)

                        allMovies.addAll(movies.results)
                    }
                } catch (e: IOException) {
                    // Handle IO exception
                }
            }
            _movies.postValue(allMovies)
        }
    }

    fun searchMovies(query: String) {
        // Launch a coroutine to perform the network request asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/search/movie?query=$query&include_adult=false&language=en-US&page=1")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val gson = Gson()
                    val movies = gson.fromJson(responseBody, MovieList::class.java)

                    _movies.postValue(movies.results)
                }
            } catch (e: IOException) {
                // Handle IO exception
            }
        }
    }
}
