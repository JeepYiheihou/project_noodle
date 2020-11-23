package com.example.projectnoodle

import android.media.MediaPlayer
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoPlayerViewModel : ViewModel(){
    val mediaPlayer = CustomMediaPlayer()
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility : LiveData<Int> get() = _progressBarVisibility

    private val _videoResolution = MutableLiveData(Pair(0, 0))
    val videoResolution : LiveData<Pair<Int, Int>> = _videoResolution

    fun loadVideo() {
        mediaPlayer.apply {
            reset()
            _progressBarVisibility.postValue(View.VISIBLE)
            setDataSource("https://jiachenbai.com:3000/api/video/sample.mp4?name=admin")
            setOnPreparedListener {
                _progressBarVisibility.postValue(View.INVISIBLE)
                isLooping = true
                it.start()
            }
            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.postValue(Pair(width, height))
            }
            setOnErrorListener { _, p1, p2 ->
                false
            }
            prepareAsync()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}