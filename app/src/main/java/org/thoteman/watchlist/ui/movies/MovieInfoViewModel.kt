package org.thoteman.watchlist.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thoteman.watchlist.BuildConfig
import org.thoteman.watchlist.model.MovieInfo
import org.thoteman.watchlist.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    private val db = FirebaseFirestore.getInstance()

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

    // Add a movie to the watchlist
    fun addToWatchList(userId: String, movieId: Int) {
        Log.d("Debug", "movieId: $movieId, userId: $userId")

        // Use FieldValue.arrayUnion to add the movieId to the existing array in the document
        val watchListUpdate = hashMapOf(
            "movieIds" to FieldValue.arrayUnion(movieId)
        )

        // Update the document with the new movieId
        db.collection("watchlists").document(userId)
            .update(watchListUpdate as Map<String, Any>)
            .addOnSuccessListener { Log.d("debug", "Document successfully updated!") }
            .addOnFailureListener { e -> Log.w("debug", "Error updating document", e) }
    }


    // Submit a review
    //fun submitReview(userId: String, movieId: Int, reviewScore: Int, reviewText: String) {
    //    val database = FirebaseDatabase.getInstance().reference
    //    val review = Review(userId, movieId, reviewScore, reviewText)
    //    database.child("reviews").push().setValue(review)
    //}

}
