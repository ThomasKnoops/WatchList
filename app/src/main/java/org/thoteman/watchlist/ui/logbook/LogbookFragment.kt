package org.thoteman.watchlist.ui.logbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.thoteman.watchlist.R
import org.thoteman.watchlist.databinding.FragmentLogbookBinding

class LogbookFragment : Fragment() {

    private var _binding: FragmentLogbookBinding? = null
    private val binding get() = _binding!!
    private lateinit var logbookViewModel: LogbookViewModel
    private lateinit var logbookAdapter: LogbookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogbookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        logbookViewModel = ViewModelProvider(this).get(LogbookViewModel::class.java)

        // Observe the LiveData containing reviews
        logbookViewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            logbookAdapter.submitList(reviews)
        }

        // Setup RecyclerView
        val recyclerView: RecyclerView = root.findViewById(R.id.logbookRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Initialize the LogbookAdapter with an empty list
        logbookAdapter = LogbookAdapter(requireContext(), emptyList())

        // Set the adapter for the RecyclerView
        recyclerView.adapter = logbookAdapter

        return root
    }

    override fun onResume() {
        super.onResume()
        // Fetch movies explicitly when the fragment becomes visible
        logbookViewModel.updateReviews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
