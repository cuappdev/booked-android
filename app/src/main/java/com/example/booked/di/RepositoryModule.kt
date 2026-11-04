package com.example.booked.di

import com.example.booked.data.repository.SampleStudyRoomRepository
import com.example.booked.data.repository.StudyRoomRepository
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
    abstract fun bindStudyRoomRepository(repository: SampleStudyRoomRepository): StudyRoomRepository
}
