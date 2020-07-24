package com.pgotuzzo.mvpreddit.model.data.preferences.android

import android.content.Context
import android.content.SharedPreferences
import com.pgotuzzo.mvpreddit.model.data.preferences.Preferences

class AndroidPreferences(context: Context) :
    Preferences {
    companion object {
        private const val PREFERENCES_NAME = "com.pgotuzzo.mvpreddit.SHARED_PREFERENCES"
    }

    private val appSharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override suspend fun save(key: Key, value: Array<String>) {
        appSharedPrefs.edit().apply { putStringSet(key.name, value.toSet()) }.apply()
    }

    override suspend fun loadStringArray(key: Key): Array<String> =
        appSharedPrefs.getStringSet(key.name, emptySet())?.toTypedArray() ?: emptyArray()

    override suspend fun exists(key: Key): Boolean = appSharedPrefs.contains(key.name)
}