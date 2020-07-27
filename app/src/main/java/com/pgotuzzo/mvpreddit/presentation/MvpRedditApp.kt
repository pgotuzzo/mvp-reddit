package com.pgotuzzo.mvpreddit.presentation

import android.app.Application
import com.pgotuzzo.mvpreddit.di.component.ApplicationComponent
import com.pgotuzzo.mvpreddit.di.component.DaggerApplicationComponent
import com.pgotuzzo.mvpreddit.di.module.ApplicationModule
import com.pgotuzzo.mvpreddit.util.log.Logger
import javax.inject.Inject

class MvpRedditApp : Application() {

    private object Tag {
        val CLASS: String = MvpRedditApp::class.java.simpleName
    }

    @Inject
    lateinit var logger: Logger

    val applicationComponent: ApplicationComponent =
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()
        applicationComponent.inject(this)
        logger.i(Tag.CLASS, "Mvp Reddit Application launched")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logger.i(Tag.CLASS, "Low Memory - Device running out of memory. Free memory required")
    }
}