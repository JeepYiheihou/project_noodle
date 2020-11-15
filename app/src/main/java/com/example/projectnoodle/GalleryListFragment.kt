package com.example.projectnoodle

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.projectnoodle.databinding.FragmentGalleryListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryListFragment : Fragment() {
    private lateinit var binding: FragmentGalleryListBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        noodleViewModel.contentListLive.observe(this.requireActivity(), Observer {
            galleryListAdapter.submitList(it)
        })

        noodleViewModel.dataStatusLive.observe(this.requireActivity(), Observer {
            if (it == DataStatus.CAN_LOAD_MORE) {
                binding.galleryListFragmentSwipe.isRefreshing = false
            }
        })
    }
}