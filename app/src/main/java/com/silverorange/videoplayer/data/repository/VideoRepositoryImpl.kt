package com.silverorange.videoplayer.data.repository

import com.silverorange.videoplayer.data.remote.VideoApi
import com.silverorange.videoplayer.data.remote.VideoDtoItem
import com.silverorange.videoplayer.domain.repository.VideoRepository
import com.silverorange.videoplayer.domain.util.Resource
import java.lang.Exception
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: VideoApi
) : VideoRepository {
    override suspend fun getVideo(): Resource<List<VideoDtoItem>> {
        return try {
            Resource.Success(
                data = api.getVideos()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}