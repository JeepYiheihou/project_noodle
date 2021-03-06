package com.example.projectnoodle.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.R
import com.example.projectnoodle.databinding.FragmentMainPartBinding

class MainPartFragment : Fragment() {
    private  lateinit var binding: FragmentMainPartBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainPartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(requireActivity(),
            R.id.mainPartNavigationFragment
        )
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        noodleViewModel.updatePlayContentStatus(false)
        noodleViewModel.playContent.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_mainPartFragment_to_singleContentFragment)
            }
        }
    }
}