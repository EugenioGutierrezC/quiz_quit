package com.egc.quizquit.data.bindings

import com.egc.quizquit.data.TrivialRepository
import com.egc.quizquit.domain.ITrivialRepository
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
    abstract fun bindTrivialRepository(
        impl: TrivialRepository
    ): ITrivialRepository
}