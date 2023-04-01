package com.n27.elections.data.repositories

import android.content.SharedPreferences
import com.n27.core.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    internal suspend fun isFirstLaunch() = withContext(Dispatchers.IO) {
        !sharedPreferences.contains(Constants.LAUNCHED)
    }

    internal suspend fun saveFirstLaunchFlag() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().putBoolean(Constants.LAUNCHED, true).apply()
    }
}
