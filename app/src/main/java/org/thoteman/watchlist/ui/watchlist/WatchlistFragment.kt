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
import org.thoteman.watchlist.ui.movies.MoviesFragmentDirections
import org.thoteman.watchlist.ui.watchlist.WatchlistFragmentDirections.Companion.actionWatchlistToMovieInfoFragment

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

        watchlistViewModel =
            ViewModelProvider(this).get(WatchlistViewModel::class.java)


        // Observe the movies LiveData
        watchlistViewModel.movies.observe(viewLifecycleOwner) { movies ->
            // Update the RecyclerView when the list of movies changes
            movieAdapter.submitList(movies)
        }

        // Setup RecyclerView
        val recyclerView: RecyclerView = root.findViewById(R.id.watchlistRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Initialize the org.thoteman.watchlist.ui.movies.MovieAdapter with an empty list (or you can pass an initial list if needed)
        movieAdapter = MovieAdapter(emptyList()) { clickedMovie ->
            // Handle the click event here, e.g., navigate to MovieInfoFragment
            val movieId = clickedMovie.id

            // Use findNavController() to get the correct NavController
            // Specify the type explicitly
            val action: NavDirections = WatchlistFragmentDirections.actionWatchlistToMovieInfoFragment(movieId)
            findNavController().navigate(action)
        }

        // Set the adapter for the RecyclerView
        recyclerView.adapter = movieAdapter

        return root
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}