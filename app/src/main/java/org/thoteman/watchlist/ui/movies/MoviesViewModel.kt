package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        // Launch a coroutine to perform the network request asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc")
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
                    val movies = gson.fromJson(responseBody, MovieList::class.java)

                    // Now, you have the list of Movie objects
                    // You can update LiveData or perform any other actions with the movies
                    _movies.postValue(movies.results)
                } else {
                    // Handle non-successful response (e.g., show an error message)
                }
            } catch (e: IOException) {
                // Handle IO exception
            }
        }
    }
}
