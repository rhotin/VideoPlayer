package com.silverorange.videoplayer.domain.repository

import com.silverorange.videoplayer.data.remote.VideoDtoItem
import com.silverorange.videoplayer.domain.util.Resource

interface VideoRepository {
    suspend fun getVideo(): Resource<List<VideoDtoItem>>
}