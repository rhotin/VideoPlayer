package com.silverorange.videoplayer

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.silverorange.videoplayer.data.local.VideoItem
import com.silverorange.videoplayer.data.local.VideoState
import com.silverorange.videoplayer.domain.repository.VideoRepository
import com.silverorange.videoplayer.domain.util.Resource
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val repository: VideoRepository
) : ViewModel() {

    var state by mutableStateOf(VideoState())
        private set

    var libraryIndex by mutableStateOf(0)
        private set
    var isPlaying by mutableStateOf(false)
        private set
    var hasNextVideo by mutableStateOf(false)
        private set
    var hasPreviousVideo by mutableStateOf(false)
        private set

    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    val videoItems = videoUris.map { uris ->
        uris.map { uri ->
            VideoItem(
                contentUri = uri,
                mediaItem = MediaItem.fromUri(uri)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        player.prepare()
    }

    fun addVideoUri(uri: Uri) {
        savedStateHandle["videoUris"] = videoUris.value + uri
        player.addMediaItem(MediaItem.fromUri(uri))
        checkMedia()
    }

    fun playVideo() {
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        isPlaying = isPlaying.not()
    }

    fun playNextVideo() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
            libraryIndex++
            checkMedia()
        }
    }

    fun playPreviousVideo() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
            libraryIndex--
            checkMedia()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun checkMedia() {
        hasNextVideo = player.hasNextMediaItem()
        hasPreviousVideo = player.hasPreviousMediaItem()
    }

    fun getVideos() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = repository.getVideo()) {
                is Resource.Error -> {
                    state = state.copy(
                        videoDto = null,
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Success -> {
                    state = state.copy(
                        videoDto = result.data?.sortedWith(compareBy {
                            it.publishedAt
                        }),
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
}