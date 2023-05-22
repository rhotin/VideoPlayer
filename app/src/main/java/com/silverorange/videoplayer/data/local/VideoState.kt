package com.silverorange.videoplayer.data.local

import com.silverorange.videoplayer.data.remote.VideoDtoItem

data class VideoState(
    val videoDto: List<VideoDtoItem>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
