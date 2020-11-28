package com.example.projectnoodle.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projectnoodle.PlayerStatus
import com.example.projectnoodle.R
import com.example.projectnoodle.VideoPlayerViewModel
import com.example.projectnoodle.databinding.FragmentSingleContentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SingleContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleContentFragment : Fragment() {
    private lateinit var binding: FragmentSingleContentBinding

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
        with (videoPlayerViewModel) {
            progressBarVisibility.observe(viewLifecycleOwner, Observer {
                binding.singleContentVideoLoadingProgressBar.visibility = it
            })

            videoResolutionLive.observe(viewLifecycleOwner, Observer {
                binding.videoControllerFrame.videoControllerSeekBar.max = mediaPlayer.duration
            })

            controllerFrameVisibility.observe(viewLifecycleOwner, Observer {
                binding.videoControllerFrame.videoControllerFrameLayout.visibility = it
            })

            bufferPercentLive.observe(viewLifecycleOwner, Observer {
                binding.videoControllerFrame.videoControllerSeekBar.apply {
                    this.secondaryProgress = this.max * it / 100
                }
            })

            playerStatus.observe(viewLifecycleOwner, Observer {
                val button = binding.videoControllerFrame.videoControllerButton
                button.isClickable = true
                when (it) {
                    PlayerStatus.PAUSED ->
                        button.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    PlayerStatus.COMPLETED ->
                        button.setImageResource(R.drawable.ic_baseline_replay_24)
                    PlayerStatus.NOT_READY ->
                        button.isClickable = false
                    else ->
                        button.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            })
        }

        binding.singleContentVideoFrameLayout.setOnClickListener {
            videoPlayerViewModel.toggleControllerVisibility()
        }

        binding.singleContentVideoSurfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) { }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                videoPlayerViewModel.mediaPlayer.setDisplay(p0)
                videoPlayerViewModel.mediaPlayer.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) { }
        })

        binding.videoControllerFrame.videoControllerButton.setOnClickListener {
            videoPlayerViewModel.togglePlayerStatus()
        }

        binding.videoControllerFrame.videoControllerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoPlayerViewModel.seekToProgress(progress)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })

        binding.root.viewTreeObserver.addOnWindowFocusChangeListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.root.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }

        lifecycle.addObserver(videoPlayerViewModel.mediaPlayer)
        updatePlayerProgress()
    }

    private fun updatePlayerProgress() {
        lifecycleScope.launch {
            while (true) {
                delay(500)
                binding.videoControllerFrame.videoControllerSeekBar.progress = videoPlayerViewModel.mediaPlayer.currentPosition
            }
        }
    }
}