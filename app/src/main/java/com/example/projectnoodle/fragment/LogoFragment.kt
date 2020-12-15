package com.example.projectnoodle.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.projectnoodle.HTTP_QUERY_LOGO_API_PREFIX
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.R
import com.example.projectnoodle.databinding.FragmentLogoBinding
import kotlinx.android.synthetic.main.gallery_cell.view.*
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
        Glide.with(binding.logoImageView)
            .load(HTTP_QUERY_LOGO_API_PREFIX)
            .placeholder(R.drawable.logo_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(binding.logoImageView)

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