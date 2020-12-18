package com.example.projectnoodle.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.projectnoodle.GalleryListAdapter
import com.example.projectnoodle.NetworkStatus
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.databinding.FragmentGalleryListBinding

class GalleryListFragment : Fragment() {
    private lateinit var binding: FragmentGalleryListBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGalleryListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val galleryListAdapter = GalleryListAdapter(noodleViewModel)
        binding.galleryListRecyclerView.apply {
            adapter = galleryListAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.galleryListFragmentSwipe.setOnRefreshListener {
            noodleViewModel.resetQuery()
        }

        noodleViewModel.contentListLive.observe(viewLifecycleOwner, Observer {
            galleryListAdapter.submitList(it)
        })

        noodleViewModel.networkStatus.observe(viewLifecycleOwner, Observer {
            galleryListAdapter.updateNetworkStatus(it)
            binding.galleryListFragmentSwipe.isRefreshing = it == NetworkStatus.INITIAL_LOADING
        })
    }
}