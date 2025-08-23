package com.example.ollamalite.di

import com.example.ollamalite.data.api.OllamaApi
import com.example.ollamalite.data.local.UserPreferencesRepository
import com.example.ollamalite.data.repository.OllamaRepository
import com.example.ollamalite.data.repository.OllamaRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DEFAULT_BASE_URL = "http://10.0.2.2:11434"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        userPreferencesRepository: UserPreferencesRepository
    ): Retrofit {
        val baseUrl = runBlocking {
            userPreferencesRepository.serverUrl.first() ?: DEFAULT_BASE_URL
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideOllamaApi(retrofit: Retrofit): OllamaApi {
        return retrofit.create(OllamaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOllamaRepository(api: OllamaApi): OllamaRepository {
        return OllamaRepositoryImpl(api)
    }
}
