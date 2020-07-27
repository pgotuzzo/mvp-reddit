package com.pgotuzzo.mvpreddit.di.module

import com.pgotuzzo.mvpreddit.di.scope.PerApplication
import com.pgotuzzo.mvpreddit.model.data.post.PostRepository
import com.pgotuzzo.mvpreddit.model.domain.DefaultPostService
import com.pgotuzzo.mvpreddit.model.domain.PostService
import com.pgotuzzo.mvpreddit.util.log.Logger
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
class DomainModule {

    @PerApplication
    @Provides
    fun providePostService(postRepository: PostRepository, logger: Logger): PostService =
        DefaultPostService(postRepository, logger)
}