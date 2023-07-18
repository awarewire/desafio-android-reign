package com.example.desafio.presentation.di

import android.content.Context
import androidx.room.Room
import com.example.desafio.data.datasource.db.AppDatabase
import com.example.desafio.data.datasource.db.HitsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesNiaDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()


    @Provides
    fun providesHitsDao(
        database: AppDatabase,
    ): HitsDao = database.hitsDao()
}
