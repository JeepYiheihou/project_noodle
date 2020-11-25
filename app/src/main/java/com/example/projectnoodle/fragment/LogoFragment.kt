package com.example.projectnoodle.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.R
import com.example.projectnoodle.databinding.FragmentLogoBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [LogoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogoFragment : Fragment() {
    private lateinit var binding: FragmentLogoBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycleScope.launch {
            delay(3000)
            /* First, observe the isLoggedInLive data. Define the behaviors */
            val isLoggedIn = noodleViewModel.isLoggedInLive.value
            noodleViewModel.updateLoginStatus(isLoggedIn?: false)
            if (noodleViewModel.isLoggedInLive.value == false) {
                findNavController().navigate(R.id.action_logoFragment_to_loginFragment)
            }
        }
    }
}