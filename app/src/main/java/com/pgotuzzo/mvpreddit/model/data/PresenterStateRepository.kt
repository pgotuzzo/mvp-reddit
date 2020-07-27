package com.pgotuzzo.mvpreddit.model.data

import android.app.Activity
import android.os.Parcelable

class PresenterStateRepository(
    private val activity: Activity,
    private val presenterId: String
) : ActivityStateRepository {

    override fun <T : Parcelable> load(): T? =
        activity.intent.getParcelableExtra<T>(presenterId)

    override fun <T : Parcelable> save(data: T) {
        activity.intent.putExtra(presenterId, data)
    }

    override fun clear() {
        activity.intent.removeExtra(presenterId)
    }
}