package com.sahilpc.bookie.di

import android.app.Application
import androidx.room.Room
import com.sahilpc.bookie.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(app:Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "notes_db"
        ).build()
    }

}