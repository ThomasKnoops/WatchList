package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import org.thoteman.watchlist.R
import org.thoteman.watchlist.databinding.FragmentMovieInfoBinding

class MovieInfoFragment : Fragment() {

    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieInfoViewModel
    private lateinit var watchlistButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        watchlistButton = root.findViewById(R.id.buttonWatchlist)
        watchlistButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                viewModel.toggleWatchList(userId , MovieInfoFragmentArgs.fromBundle(requireArguments()).movieId)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = MovieInfoFragmentArgs.fromBundle(requireArguments()).movieId

        viewModel = ViewModelProvider(this, MovieInfoViewModelFactory(movieId))
            .get(MovieInfoViewModel::class.java)

        // Set the initial button text based on the watchlist status
        viewModel.isInWatchlist.observe(viewLifecycleOwner) { isInWatchlist ->
            val buttonText = if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist"
            watchlistButton.text = buttonText
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movie.observe(viewLifecycleOwner) { movieInfo ->
            // Update UI components using the movieInfo data
            binding.textViewTitle.text = movieInfo.title
            binding.textViewTagline.text = movieInfo.tagline
            binding.textViewOverview.text = movieInfo.overview
            binding.textViewPopularity.text = "Popularity: ${movieInfo.popularity.toString()}"
            binding.textViewDuration.text = "Runtime: ${movieInfo.runtime.toString()}"
            binding.textViewVoteAverage.text = "Rating: ${movieInfo.vote_average.toString()}"
            binding.textViewVoteCount.text = "Rating Count: ${movieInfo.vote_count.toString()}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
