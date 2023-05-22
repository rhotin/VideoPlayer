package com.silverorange.videoplayer.data.remote

import retrofit2.http.GET

interface VideoApi {

    companion object{
        const val BASE_URL = "http://10.0.2.2:4000/"
    }

    @GET("videos")
    suspend fun getVideos(): List<VideoDtoItem>
}