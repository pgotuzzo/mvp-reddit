package com.pgotuzzo.mvpreddit.model.data.preferences

import com.pgotuzzo.mvpreddit.model.data.preferences.android.Key

interface Preferences {

    /**
     * Saves [value]. Making it accessible through [key]
     * @see loadStringArray
     * @param key id used to reference the data saved
     * @param value data to be stored
     */
    suspend fun save(key: Key, value: Array<String>)

    /**
     * Loads stored data, if any. Searching and accessing it through [key]
     * @see save
     * @param key id used to reference the data saved
     * @return data previously stored
     */
    suspend fun loadStringArray(key: Key): Array<String>

    /**
     * Check if there is data stored associated to the [key]
     * @param key id used to reference the data previously saved
     * @return true if there is data stored associated
     */
    suspend fun exists(key: Key): Boolean
}