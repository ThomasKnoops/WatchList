package org.thoteman.watchlist.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.thoteman.watchlist.R
import org.thoteman.watchlist.model.MovieInfo

class MovieAdapter(private val movies: List<MovieInfo>, private val onItemClickListener: (MovieInfo) -> Unit) : ListAdapter<MovieInfo, MovieAdapter.MovieViewHolder>(
    MovieDiffCallback()
) {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        val ratingCountTextView: TextView = itemView.findViewById(R.id.ratingCountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.titleTextView.text = movie.title
        holder.ratingTextView.text = holder.itemView.context.getString(R.string.rating_format, movie.vote_average.toString())
        holder.ratingCountTextView.text = holder.itemView.context.getString(R.string.rating_count_format, movie.vote_count.toString())

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onItemClickListener(movie)
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieInfo>() {
    override fun areItemsTheSame(oldItem: MovieInfo, newItem: MovieInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieInfo, newItem: MovieInfo): Boolean {
        return oldItem == newItem
    }
}
