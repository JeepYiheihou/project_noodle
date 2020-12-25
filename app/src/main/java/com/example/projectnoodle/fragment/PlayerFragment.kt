package com.example.projectnoodle.fragment

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.projectnoodle.databinding.FragmentPlayerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerFragment(private val url: String) : Fragment() {
    lateinit var binding: FragmentPlayerBinding

    private val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer.apply {
            setOnPreparedListener {
                binding.playerProgressBar.max = mediaPlayer.duration
                start()
                binding.playerReadyBar.visibility = View.INVISIBLE
            }
            setDataSource(url)
            prepareAsync()
            binding.playerProgressBar.visibility = View.VISIBLE
        }
        lifecycleScope.launch {
            while (true) {
                binding.playerProgressBar.progress = mediaPlayer.currentPosition
                delay(500)
            }
        }
        binding.playerSurfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) { }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                mediaPlayer.setDisplay(p0)
                mediaPlayer.setScreenOnWhilePlaying(true)
            }
            override fun surfaceDestroyed(p0: SurfaceHolder) { }
        })
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }
}