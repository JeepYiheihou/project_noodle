package com.example.projectnoodle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectnoodle.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_single_content.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noodleviewModel: NoodleViewModel
    private lateinit var videoPlayerViewModel: VideoPlayerViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Remove the default title bar */
        supportActionBar?.hide();
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        noodleviewModel = ViewModelProvider(this).get(NoodleViewModel::class.java)
        videoPlayerViewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}