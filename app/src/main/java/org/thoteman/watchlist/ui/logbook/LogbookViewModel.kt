package org.thoteman.watchlist.ui.logbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.thoteman.watchlist.model.MovieReview

class LogbookViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _reviews = MutableLiveData<List<MovieReview>>()
    val reviews: LiveData<List<MovieReview>> = _reviews

    init {
        updateReviews()
    }

    // Function to update reviews
    fun updateReviews() {
        // Get the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Check if the user is logged in
        if (userId != null) {
            val userReviewsQuery = db.collection("reviews").whereEqualTo("userId", userId)
            val newReviews = mutableListOf<MovieReview>()

            userReviewsQuery.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val review = document.toObject(MovieReview::class.java)
                        //Log.d("debug", review.toString())
                        // Process the review data
                        if (review != null) {
                            newReviews.add(review)
                        }
                    }
                    _reviews.postValue(newReviews)
                }
        }
    }
}