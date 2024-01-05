package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thoteman.watchlist.BuildConfig
import org.thoteman.watchlist.model.MovieInfo
import java.io.IOException

class MovieInfoViewModel(movieId: Int) : ViewModel() {
    private val _movie = MutableLiveData<MovieInfo>().apply {
        value = MovieInfo(
            id = movieId,
            title = "Loading...",
            tagline = "",
            overview = "",
            popularity = 0.0f,
            release_date = "",
            runtime = 0,
            vote_average = 0.0f,
            vote_count = 0
        )
    }
    val movie: LiveData<MovieInfo> = _movie

    private val client = OkHttpClient()

    init {
        // Use viewModelScope to launch the coroutine
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/${movieId}?language=en-US")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                    .build()

                val response = client.newCall(request).execute()

                // Check if the response is successful
                if (response.isSuccessful) {
                    // Get the response body as a string
                    val responseBody = response.body?.string()

                    // Use Gson to parse the JSON string into a list of Movie objects
                    val gson = Gson()
                    val movie = gson.fromJson(responseBody, MovieInfo::class.java)

                    // Now, you have the list of Movie objects
                    // You can update LiveData or perform any other actions with the movies
                    _movie.postValue(movie)
                } else {
                    // Handle non-successful response (e.g., show an error message)
                }
            } catch (e: IOException) {
                // Handle IO exception
            }
        }
    }
}
