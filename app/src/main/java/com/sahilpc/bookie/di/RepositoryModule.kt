package com.sahilpc.bookie.di

import com.sahilpc.bookie.data.repository.AuthRepositoryImpl
import com.sahilpc.bookie.data.repository.NoteRepositoryImpl
import com.sahilpc.bookie.domain.repository.AuthRepository
import com.sahilpc.bookie.domain.repository.NoteRepository
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
    abstract fun bindAuthRepository(
       authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository

}