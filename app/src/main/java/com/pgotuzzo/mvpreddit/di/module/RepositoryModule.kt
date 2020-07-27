package com.pgotuzzo.mvpreddit.di.module

import com.pgotuzzo.mvpreddit.di.scope.PerApplication
import com.pgotuzzo.mvpreddit.model.data.post.DefaultPostRepository
import com.pgotuzzo.mvpreddit.model.data.post.PostRepository
import com.pgotuzzo.mvpreddit.model.data.post.reddit.RedditService
import com.pgotuzzo.mvpreddit.model.data.preferences.Preferences
import dagger.Module
import dagger.Provides

@Module(includes = [DataSourceModule::class])
class RepositoryModule {

    @PerApplication
    @Provides
    fun providePostRepository(
        preferences: Preferences,
        redditService: RedditService
    ): PostRepository =
        DefaultPostRepository(
            preferences,
            redditService
        )
}