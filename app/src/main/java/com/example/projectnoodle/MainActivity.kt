package com.example.projectnoodle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projectnoodle.databinding.ActivityMainBinding
import com.example.projectnoodle.databinding.FragmentLogoBinding
import kotlinx.android.synthetic.main.fragment_single_content.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noodleViewModel: NoodleViewModel
    private lateinit var videoPlayerViewModel: VideoPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Remove the default title bar */
        supportActionBar?.hide();
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        noodleViewModel = ViewModelProvider(this).get(NoodleViewModel::class.java)
        videoPlayerViewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.rootNavigationFragment)
        return controller.navigateUp()
    }
}