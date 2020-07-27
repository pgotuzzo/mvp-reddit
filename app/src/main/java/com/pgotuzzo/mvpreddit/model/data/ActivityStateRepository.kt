package com.pgotuzzo.mvpreddit.model.data

import android.os.Parcelable

/**
 * Repository in charge of saving Activity state
 * Persists and retrieves states while the Activity is alive.
 * Information persisted will survive if the OS kills the Application process temporally (kill and
 * then restarts)
 */
interface ActivityStateRepository {

    /**
     * Loads state [T]
     */
    fun <T : Parcelable> load(): T?

    /**
     * Stores state [T]
     * @param data state to be stored
     */
    fun <T : Parcelable> save(data: T)

    /**
     * Deletes any previously stored state
     */
    fun clear()
}