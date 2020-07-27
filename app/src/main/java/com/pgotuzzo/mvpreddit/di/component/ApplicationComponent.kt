package com.pgotuzzo.mvpreddit.di.component

import com.pgotuzzo.mvpreddit.di.module.ApplicationModule
import com.pgotuzzo.mvpreddit.di.module.DomainModule
import com.pgotuzzo.mvpreddit.di.scope.PerApplication
import com.pgotuzzo.mvpreddit.model.domain.PostService
import com.pgotuzzo.mvpreddit.presentation.MvpRedditApp
import com.pgotuzzo.mvpreddit.util.log.Logger
import dagger.Component

@PerApplication
@Component(modules = [ApplicationModule::class, DomainModule::class])
interface ApplicationComponent {

    // Singletons provided
    fun postService(): PostService

    fun logger(): Logger

    // Injections
    fun inject(mvpRedditApp: MvpRedditApp)
}