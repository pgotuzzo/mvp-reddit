package com.pgotuzzo.mvpreddit.di.module

import android.content.Context
import com.pgotuzzo.mvpreddit.di.scope.PerApplication
import com.pgotuzzo.mvpreddit.util.log.DefaultLogger
import com.pgotuzzo.mvpreddit.util.log.Logger
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {

    @PerApplication
    @Provides
    fun provideContext(): Context = context

    @PerApplication
    @Provides
    fun provideLogger(): Logger = DefaultLogger()
}