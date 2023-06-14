package com.n27.elections.data.repositories

import android.content.SharedPreferences
import com.n27.core.Constants.LAUNCHED
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    internal fun isFirstLaunch(): Boolean = !sharedPreferences.contains(LAUNCHED)

    internal fun saveFirstLaunchFlag() { sharedPreferences.edit().putBoolean(LAUNCHED, true).apply() }
}
