package org.thoteman.watchlist.ui.watchlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thoteman.watchlist.BuildConfig
import org.thoteman.watchlist.model.Movie
import org.thoteman.watchlist.model.MovieInfo
import java.io.IOException

class WatchlistViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _movies = MutableLiveData<List<Movie>>()
    private var lastFetchedMovieIds: List<Int>? = null
    private val client = OkHttpClient()

    val movies: LiveData<List<Movie>> = _movies

    init {
        refreshMovies()
    }

    fun refreshMovies() {
        // Get the current user's ID (you need to replace this with your actual logic to get the user ID)
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Check if the user is logged in
        if (userId != null) {
            // Retrieve the movieIds from Firestore
            db.collection("watchlists").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Get the list of movieIds
                        val movieIds = documentSnapshot["movieIds"] as? List<Int>

                        // Check if the movieIds are different from the last fetched ones
                        if (movieIds != lastFetchedMovieIds) {
                            // Fetch movie details for each movieId
                            if (movieIds != null) {
                                lastFetchedMovieIds = movieIds
                                fetchMovies(movieIds)
                            }
                        }
                    } else {
                        Log.d("debug", "No watchlist document found for user $userId")
                        // Update LiveData with an empty list if no watchlist document is found
                        _movies.postValue(emptyList())
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("debug", "Error getting watchlist document", e)
                    // Update LiveData with an empty list in case of failure
                    _movies.postValue(emptyList())
                }
        }
    }

    private fun fetchMovies(movieIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            val moviesList = fetchMoviesApi(movieIds)

            // Update LiveData with the new list of movies in the main thread
            _movies.postValue(moviesList)
        }
    }

    private suspend fun fetchMoviesApi(movieIds: List<Int>): List<Movie> {
        // Create a list to store Movie objects
        val moviesList = mutableListOf<Movie>()

        // Use async to perform asynchronous calls and gather the results
        val deferredMovies = movieIds.map { movieId ->
            viewModelScope.async(Dispatchers.IO) {
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

                        // Use Gson to parse the JSON string into a Movie object
                        val gson = Gson()
                        val movie = gson.fromJson(responseBody, Movie::class.java)

                        // Add the movie to the list
                        moviesList.add(movie)
                    } else {
                        // Handle non-successful response (e.g., show an error message)
                    }
                } catch (e: IOException) {
                    // Handle IO exception
                }
            }
        }

        // Wait for all async calls to complete
        deferredMovies.awaitAll()

        return moviesList
    }

}
