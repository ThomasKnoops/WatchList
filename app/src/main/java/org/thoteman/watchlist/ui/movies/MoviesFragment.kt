package org.thoteman.watchlist.ui.movies

import MovieAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.thoteman.watchlist.R
import org.thoteman.watchlist.databinding.FragmentMoviesBinding

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        moviesViewModel =
            ViewModelProvider(this).get(MoviesViewModel::class.java)

        // Observe the movies LiveData
        moviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
            // Update the RecyclerView when the list of movies changes
            movieAdapter.submitList(movies)
        }

        // Setup RecyclerView
        val recyclerView: RecyclerView = root.findViewById(R.id.moviesRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Initialize the MovieAdapter with an empty list (or you can pass an initial list if needed)
        movieAdapter = MovieAdapter(emptyList())
        recyclerView.adapter = movieAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
