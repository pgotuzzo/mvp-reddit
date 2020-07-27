package com.pgotuzzo.mvpreddit.presentation.ktx

import android.app.Activity
import com.pgotuzzo.mvpreddit.di.component.ApplicationComponent
import com.pgotuzzo.mvpreddit.presentation.MvpRedditApp

val Activity.applicationComponent: ApplicationComponent
    get() = (application as MvpRedditApp).applicationComponent
