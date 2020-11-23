package com.example.projectnoodle

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.projectnoodle.databinding.FragmentGalleryListBinding
import com.example.projectnoodle.databinding.FragmentSingleContentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SingleContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleContentFragment : Fragment() {
    private lateinit var binding: FragmentSingleContentBinding

    private val noodleViewModel: NoodleViewModel by activityViewModels()
    private val videoPlayerViewModel: VideoPlayerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleContentBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        videoPlayerViewModel.progressBarVisibility.observe(requireActivity(), Observer {
            binding.singleContentVideoLoadingProgressBar.visibility = it
        })

        videoPlayerViewModel.videoResolution.observe(requireActivity(), Observer {
            binding.singleContentVideoFrameLayout.post {
                resizePlayer(it.first, it.second)
            }
        })

        binding.singleContentVideoSurfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) { }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                videoPlayerViewModel.mediaPlayer.setDisplay(p0)
                videoPlayerViewModel.mediaPlayer.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) { }
        })
        lifecycle.addObserver(videoPlayerViewModel.mediaPlayer)
        videoPlayerViewModel.loadVideo()
    }

    private fun resizePlayer(width: Int, height: Int) {
        if (width == 0 || height == 0) return
        binding.singleContentVideoSurfaceView.layoutParams = FrameLayout.LayoutParams(
            binding.singleContentFragmentLayout.height * width / height,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
    }
}