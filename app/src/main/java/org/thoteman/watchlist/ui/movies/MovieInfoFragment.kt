package org.thoteman.watchlist.ui.movies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import org.thoteman.watchlist.R
import org.thoteman.watchlist.databinding.FragmentMovieInfoBinding
import org.thoteman.watchlist.ui.logbook.LogbookAdapter
import org.thoteman.watchlist.ui.logbook.LogbookViewModel

class MovieInfoFragment : Fragment() {

    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieInfoViewModel
    private lateinit var reviewAdapter: LogbookAdapter
    private lateinit var watchlistButton: Button
    private lateinit var reviewButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this, MovieInfoViewModelFactory(MovieInfoFragmentArgs.fromBundle(requireArguments()).movieId))
            .get(MovieInfoViewModel::class.java)

        watchlistButton = root.findViewById(R.id.buttonWatchlist)
        watchlistButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                viewModel.toggleWatchList(userId , MovieInfoFragmentArgs.fromBundle(requireArguments()).movieId)
            }
        }

        reviewButton = root.findViewById(R.id.buttonReview)
        reviewButton.setOnClickListener {
            viewModel.reviewDialog(requireContext(), MovieInfoFragmentArgs.fromBundle(requireArguments()).movieId, MovieInfoFragmentArgs.fromBundle(requireArguments()).movieTitle)
        }

        // Observe the LiveData containing reviews
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewAdapter.submitList(reviews)
        }

        // Setup RecyclerView
        val recyclerView: RecyclerView = root.findViewById(R.id.reviewRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Initialize the LogbookAdapter with an empty list
        reviewAdapter = LogbookAdapter(requireContext(), emptyList())

        // Set the adapter for the RecyclerView
        recyclerView.adapter = reviewAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = MovieInfoFragmentArgs.fromBundle(requireArguments()).movieId

        viewModel = ViewModelProvider(this, MovieInfoViewModelFactory(movieId))
            .get(MovieInfoViewModel::class.java)

        // Set the initial button text based on the watchlist status
        viewModel.isInWatchlist.observe(viewLifecycleOwner) { isInWatchlist ->
            val buttonText = if (isInWatchlist) getString(R.string.remove_from_watchlist) else getString(R.string.add_to_watchlist)
            watchlistButton.text = buttonText
        }

        // Set all the information
        binding.textViewTitle.text = MovieInfoFragmentArgs.fromBundle(requireArguments()).movieTitle
        binding.textViewTagline.text = MovieInfoFragmentArgs.fromBundle(requireArguments()).movieTagline
        binding.textViewOverview.text = MovieInfoFragmentArgs.fromBundle(requireArguments()).movieOverview
        binding.textViewPopularity.text = getString(R.string.release_date, MovieInfoFragmentArgs.fromBundle(requireArguments()).movieReleaseDate)
        binding.textViewVoteAverage.text = getString(R.string.rating_format, MovieInfoFragmentArgs.fromBundle(requireArguments()).movieVoteAverage.toString())
        binding.textViewVoteCount.text = getString(R.string.rating_count_format, MovieInfoFragmentArgs.fromBundle(requireArguments()).movieVoteCount.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

