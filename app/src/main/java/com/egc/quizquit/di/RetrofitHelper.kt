package com.egc.quizquit.di

import com.egc.quizquit.network.APITrivial
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitHelper {
    companion object {
        private const val BASE_URL = "https://opentdb.com/"
    }

    @Singleton
    @Provides
    fun providerRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMarvelCharactersClient(retrofit: Retrofit): APITrivial {
        return retrofit.create(APITrivial::class.java)
    }
}