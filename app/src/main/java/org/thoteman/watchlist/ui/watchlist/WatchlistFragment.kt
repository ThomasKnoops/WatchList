package org.thoteman.watchlist.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.thoteman.watchlist.R
import org.thoteman.watchlist.databinding.FragmentWatchlistBinding
import org.thoteman.watchlist.ui.movies.MovieAdapter

class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var watchlistViewModel: WatchlistViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup RecyclerView
        val recyclerView: RecyclerView = root.findViewById(R.id.watchlistRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Initialize the MovieAdapter with an empty list
        movieAdapter = MovieAdapter(emptyList()) { clickedMovie ->
            val action: NavDirections = WatchlistFragmentDirections.actionWatchlistToMovieInfoFragment(
                clickedMovie.id,
                clickedMovie.title,
                clickedMovie.tagline,
                clickedMovie.overview,
                clickedMovie.popularity,
                clickedMovie.release_date,
                clickedMovie.runtime,
                clickedMovie.vote_average,
                clickedMovie.vote_count)
            findNavController().navigate(action)
        }

        // Set the adapter for the RecyclerView
        recyclerView.adapter = movieAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        watchlistViewModel =
            ViewModelProvider(this).get(WatchlistViewModel::class.java)

        // Observe the movies LiveData
        watchlistViewModel.movies.observe(viewLifecycleOwner) { movies ->
            // Update the RecyclerView when the list of movies changes
            movieAdapter.submitList(movies)
        }
    }

    override fun onResume() {
        super.onResume()
        // Fetch movies explicitly when the fragment becomes visible
        watchlistViewModel.refreshMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
