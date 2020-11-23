package com.example.projectnoodle

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.projectnoodle.databinding.FragmentLoginBinding
import com.example.projectnoodle.databinding.FragmentLogoBinding

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

        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) { }
            override fun onFinish() {
                /* First, observe the isLoggedInLive data. Define the behaviors */
                val isLoggedIn = noodleViewModel.isLoggedInLive.value
                noodleViewModel.updateLoginStatus(isLoggedIn?: false)
            }
        }.start()
    }
}