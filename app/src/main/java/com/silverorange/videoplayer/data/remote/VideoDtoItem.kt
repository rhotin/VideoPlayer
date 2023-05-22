package com.silverorange.videoplayer.data.remote

data class VideoDtoItem(
    val author: Author,
    val description: String,
    val fullURL: String,
    val hlsURL: String,
    val id: String,
    val publishedAt: String,
    val title: String
)