package com.example.projectnoodle.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.R
import com.example.projectnoodle.databinding.FragmentAboutUserBinding
import com.example.projectnoodle.databinding.FragmentGalleryListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AboutUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutUserFragment : Fragment() {
    private lateinit var binding: FragmentAboutUserBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUserBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noodleViewModel.userLive.observe(viewLifecycleOwner, {
            binding.aboutUserText.text = "Name: ${it.name}\nGender: ${it.gender}\nEmail: ${it.email}\nCreated time: ${it.createdTime}\nStatus: ${it.status}"
        })
    }
}