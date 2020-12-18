package com.example.projectnoodle.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.adapter.VideoSwipeAdapter
import com.example.projectnoodle.databinding.FragmentLogoBinding
import com.example.projectnoodle.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private lateinit var binding: FragmentVideoBinding

    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val videoSwipeAdapter = VideoSwipeAdapter(noodleViewModel, requireActivity())
        binding.videoViewPager.apply {
            adapter = videoSwipeAdapter
        }
    }
}