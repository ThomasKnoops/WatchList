package org.thoteman.watchlist.ui.movies

import android.content.Context
import android.content.DialogInterface
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thoteman.watchlist.BuildConfig
import org.thoteman.watchlist.model.MovieInfo
import com.google.firebase.firestore.FirebaseFirestore
import org.thoteman.watchlist.R
import org.thoteman.watchlist.model.MovieReview
import java.io.IOException

class MovieInfoViewModel(movieId: Int) : ViewModel() {

    private val _isInWatchlist = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isInWatchlist: LiveData<Boolean> = _isInWatchlist

    private val db = FirebaseFirestore.getInstance()

    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Check if the user is logged in
        if (userId != null) {
            // Retrieve the movieIds from Firestore
            db.collection("watchlists").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Get the list of movieIds
                        val movieIds = documentSnapshot["movieIds"] as? List<Int>?

                        // Check if movieIds is not null and contains movieId
                        if (movieIds != null) {
                            for (id in movieIds) {
                                if (id == movieId) {
                                    _isInWatchlist.postValue(true)
                                    return@addOnSuccessListener
                                }
                            }
                            _isInWatchlist.postValue(false)
                        }
                    } else {
                        db.collection("watchlists").document(userId)
                            .set(hashMapOf("movieIds" to emptyList<Int>()))
                        _isInWatchlist.postValue(false)
                }

            }
        }
    }

    // Add a movie to the watchlist
    fun toggleWatchList(userId: String, movieId: Int) {
        val watchListUpdate: Map<String, Any>
        if (isInWatchlist.value == true) {
            watchListUpdate = hashMapOf(
                "movieIds" to FieldValue.arrayRemove(movieId)
            )
            _isInWatchlist.postValue(false)
        } else {
            watchListUpdate = hashMapOf(
                "movieIds" to FieldValue.arrayUnion(movieId)
            )
            _isInWatchlist.postValue(true)
        }

        db.collection("watchlists").document(userId)
            .update(watchListUpdate as Map<String, Any>)
    }

    private fun saveReview(movieId: Int, movieTitle: String, score: Int, review: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        // Remove from watchlist
        val watchListUpdate: Map<String, Any>
        if (isInWatchlist.value == true) {
            watchListUpdate = hashMapOf(
                "movieIds" to FieldValue.arrayRemove(movieId)
            )
            _isInWatchlist.postValue(false)
            db.collection("watchlists").document(userId)
                .update(watchListUpdate as Map<String, Any>)
        }
        // Add review
        val reviewDocumentPath = "/${userId}_$movieId"
        val newReview = MovieReview(userId, movieId, movieTitle, score, review)
        db.collection("reviews").document(reviewDocumentPath)
            .set(newReview)
    }

    fun reviewDialog(context: Context, movieId: Int, movieTitle: String) {
        val inputLayout = LinearLayout(context)
        inputLayout.orientation = LinearLayout.VERTICAL

        val scoreEditText = EditText(context)
        scoreEditText.hint = context.getString(R.string.a_score_from_0_to_10)
        // Set InputFilter to restrict input to values between 0 and 5
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            try {
                // Check if the input can be parsed as an integer
                source.toString().toInt()
                null
            } catch (e: NumberFormatException) {
                // If not, reject the input
                ""
            }
        }
        scoreEditText.filters = arrayOf(inputFilter)
        inputLayout.addView(scoreEditText)

        val reviewEditText = EditText(context)
        reviewEditText.hint = context.getString(R.string.write_a_review)
        inputLayout.addView(reviewEditText)

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(context.getString(R.string.review_movie))
        alertDialogBuilder.setView(inputLayout)

        alertDialogBuilder.setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
            if (scoreEditText.text.toString().toInt() > 10)
                scoreEditText.setText(context.getString(R.string._10))

            saveReview(movieId, movieTitle, scoreEditText.text.toString().toInt(), reviewEditText.text.toString())

            // Dismiss the alert dialog
            dialogInterface.dismiss()
        }

        alertDialogBuilder.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
            // Dismiss the alert dialog if the user clicks Cancel
            dialogInterface.dismiss()
        }

        alertDialogBuilder.show()
    }


}
