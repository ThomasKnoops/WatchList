package org.thoteman.watchlist.ui.logbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.thoteman.watchlist.R
import org.thoteman.watchlist.model.MovieReview

class LogbookAdapter(private val context: Context, private val reviews: List<MovieReview>) : ListAdapter<MovieReview, LogbookAdapter.LogbookViewHolder>(
    LogbookDiffCallback()
){
    class LogbookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitleLogbook)
        val scoreTextView: TextView = itemView.findViewById(R.id.textViewRatingLogbook)
        val reviewTextView: TextView = itemView.findViewById(R.id.textViewReviewLogbook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogbookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.logbook_card, parent, false)
        return LogbookViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogbookViewHolder, position: Int) {
        val review = getItem(position)
        holder.titleTextView.text = review.movieTitle
        holder.scoreTextView.text = context.getString(R.string.your_score, review.score.toString())
        holder.reviewTextView.text = review.reviewText
    }
}

class LogbookDiffCallback : DiffUtil.ItemCallback<MovieReview>() {
    override fun areItemsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
        return oldItem == newItem
    }
}
