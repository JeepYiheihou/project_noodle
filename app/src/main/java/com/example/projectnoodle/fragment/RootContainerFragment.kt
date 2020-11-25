package com.example.projectnoodle.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.example.projectnoodle.*
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRootContainerBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noodleViewModel.isLoggedInLive.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Biang!", Toast.LENGTH_SHORT).show()
            if ((it && !noodleViewModel.currentLoggedInStatus) || (!it && noodleViewModel.currentLoggedInStatus)) {
                val transaction = parentFragmentManager.beginTransaction()
                val targetFragment = if (!it) {
                    LoginFragment()
                } else {
                    MainPartFragment()
                }
                transaction.replace(R.id.containedFragment, targetFragment)
                transaction.commit()
            }
            noodleViewModel.currentLoggedInStatus = it
        }

        noodleViewModel.playContent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Liangsui!", Toast.LENGTH_SHORT).show()
            val transaction = parentFragmentManager.beginTransaction()
            val targetFragment = SingleContentFragment()
            transaction.replace(R.id.containedFragment, targetFragment)
            transaction.addToBackStack(targetFragment.javaClass.name);
            transaction.commit()
        }

        noodleViewModel.trytry.observe(viewLifecycleOwner) {

        }
    }
}