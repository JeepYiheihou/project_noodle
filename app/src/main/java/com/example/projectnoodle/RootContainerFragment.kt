package com.example.projectnoodle

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.projectnoodle.databinding.FragmentRootContainerBinding

/**
 * A simple [Fragment] subclass.
 * Use the [RootContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RootContainerFragment : Fragment() {
    private lateinit var binding: FragmentRootContainerBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noodleViewModel.isLoggedInLive.observe(this.requireActivity(), {
            val transaction = parentFragmentManager.beginTransaction()
            val targetFragment = if (it == false) {
                LoginFragment()
            } else {
                MainPartFragment()
            }
            transaction.replace(R.id.containedFragment, targetFragment)
            transaction.commit()
        })

        noodleViewModel.playContent.observe(this.requireActivity(), {
            val transaction = parentFragmentManager.beginTransaction()
            val targetFragment = SingleContentFragment()
            transaction.replace(R.id.containedFragment, targetFragment)
            transaction.commit()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRootContainerBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
}