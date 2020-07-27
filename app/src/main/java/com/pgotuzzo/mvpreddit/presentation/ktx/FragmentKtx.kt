package com.pgotuzzo.mvpreddit.presentation.ktx

import androidx.fragment.app.Fragment
import com.pgotuzzo.mvpreddit.di.component.ApplicationComponent

val Fragment.applicationComponent: ApplicationComponent?
    get() = activity?.applicationComponent