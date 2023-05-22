package com.silverorange.videoplayer.di

import com.silverorange.videoplayer.data.repository.VideoRepositoryImpl
import com.silverorange.videoplayer.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepository: VideoRepositoryImpl
    ): VideoRepository

}