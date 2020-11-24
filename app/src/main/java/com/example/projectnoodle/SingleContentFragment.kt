package com.example.projectnoodle

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
        with (videoPlayerViewModel) {
            progressBarVisibility.observe(requireActivity(), Observer {
                binding.singleContentVideoLoadingProgressBar.visibility = it
            })

            videoResolutionLive.observe(requireActivity(), Observer {
                binding.videoControllerFrame.videoControllerSeekBar.max = mediaPlayer.duration
                binding.singleContentVideoFrameLayout.post {
                    resizePlayer(it.first, it.second)
                }
            })

            controllerFrameVisibility.observe(requireActivity(), Observer {
                binding.videoControllerFrame.videoControllerFrameLayout.visibility = it
            })

            bufferPercentLive.observe(requireActivity(), Observer {
                binding.videoControllerFrame.videoControllerSeekBar.apply {
                    this.secondaryProgress = this.max * it / 100
                }
            })

            playerStatus.observe(requireActivity(), Observer {
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

        videoPlayerViewModel.loadVideo()
        lifecycle.addObserver(videoPlayerViewModel.mediaPlayer)
        updatePlayerProgress()
    }

    private fun resizePlayer(width: Int, height: Int) {
        return
        if (width == 0 || height == 0) return
        binding.singleContentVideoSurfaceView.layoutParams = FrameLayout.LayoutParams(
            binding.singleContentFragmentLayout.height * width / height,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
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