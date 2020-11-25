package com.example.projectnoodle

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectnoodle.customobject.CustomMediaPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PlayerStatus {
    PLAYING,
    PAUSED,
    COMPLETED,
    NOT_READY
}

class VideoPlayerViewModel : ViewModel(){
    var mediaPlayer = CustomMediaPlayer()

    private var controllerShowTime = 0L

    private val _playerStatusLive = MutableLiveData(PlayerStatus.NOT_READY)
    val playerStatus : LiveData<PlayerStatus> get() = _playerStatusLive

    private val _bufferPercentLive = MutableLiveData(0)
    val bufferPercentLive : LiveData<Int> get() = _bufferPercentLive

    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility : LiveData<Int> get() = _progressBarVisibility

    private val _controllerFrameVisibility = MutableLiveData(View.INVISIBLE)
    val controllerFrameVisibility : LiveData<Int> get() = _controllerFrameVisibility

    private val _videoResolutionLive = MutableLiveData(Pair(0, 0))
    val videoResolutionLive : LiveData<Pair<Int, Int>> = _videoResolutionLive

    fun togglePlayerStatus() {
        when (_playerStatusLive.value) {
            PlayerStatus.PLAYING -> {
                mediaPlayer.pause()
                _playerStatusLive.postValue(PlayerStatus.PAUSED)
            }
            PlayerStatus.PAUSED -> {
                mediaPlayer.start()
                _playerStatusLive.postValue(PlayerStatus.PLAYING)
            }
            PlayerStatus.COMPLETED -> {
                mediaPlayer.start()
                _playerStatusLive.postValue(PlayerStatus.PLAYING)
            }
            else -> return
        }
    }

    fun toggleControllerVisibility() {
        if (_controllerFrameVisibility.value == View.INVISIBLE) {
            _controllerFrameVisibility.postValue(View.VISIBLE)
            controllerShowTime = System.currentTimeMillis()
            viewModelScope.launch {
                delay(3000)
                if (System.currentTimeMillis() - controllerShowTime >= 2900) {
                    _controllerFrameVisibility.postValue(View.INVISIBLE)
                }
            }
        } else {
            _controllerFrameVisibility.postValue(View.INVISIBLE)
        }
    }

    fun playVideo() {
        /* Dirty hack here. Each time loading a video, we'll need to re-create the mediaPlayer.
         * Otherwise it will crash with same mediaPlayer calling prepareAsync multiple times. */
        mediaPlayer.release()
        mediaPlayer = CustomMediaPlayer()
        mediaPlayer.apply {
            reset()
            _playerStatusLive.postValue(PlayerStatus.NOT_READY)
            _progressBarVisibility.postValue(View.VISIBLE)
            setDataSource("https://jiachenbai.com:3000/api/video/sample.mp4?name=admin")
            setOnPreparedListener {
                _progressBarVisibility.postValue(View.INVISIBLE)
                it.start()
                _playerStatusLive.postValue(PlayerStatus.PLAYING)
            }

            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolutionLive.postValue(Pair(width, height))
            }

            setOnBufferingUpdateListener { _, percent ->
                _bufferPercentLive.postValue(percent)
            }

            setOnCompletionListener {
                _playerStatusLive.postValue(PlayerStatus.COMPLETED)
            }

            setOnSeekCompleteListener {
                mediaPlayer.start()
                _progressBarVisibility.postValue(View.INVISIBLE)
            }
            prepareAsync()
        }
    }

    fun seekToProgress(progress: Int) {
        _progressBarVisibility.postValue(View.VISIBLE)
        mediaPlayer.seekTo(progress)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}