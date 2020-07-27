package com.pgotuzzo.mvpreddit.di.module

import android.content.Context
import com.google.gson.GsonBuilder
import com.pgotuzzo.mvpreddit.di.scope.PerApplication
import com.pgotuzzo.mvpreddit.model.data.post.reddit.RedditService
import com.pgotuzzo.mvpreddit.model.data.preferences.Preferences
import com.pgotuzzo.mvpreddit.model.data.preferences.android.AndroidPreferences
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [ApplicationModule::class])
class DataSourceModule {

    @PerApplication
    @Provides
    fun provideRedditService(): RedditService {
        val retrofit = Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        return retrofit.create(RedditService::class.java)
    }

    @PerApplication
    @Provides
    fun providePreferences(context: Context): Preferences = AndroidPreferences(context)
}