package com.example.desafio.presentation.di

import com.example.desafio.BuildConfig
import com.example.desafio.data.datasource.device.AndroidNetworkHandler
import com.example.desafio.data.datasource.device.NetworkHandler
import com.example.desafio.data.datasource.remote.AppApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val okHttpClient: OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun appApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }

    @Provides
    fun provideNetworkHandler(networkHandler: AndroidNetworkHandler): NetworkHandler {
        return networkHandler
    }
}