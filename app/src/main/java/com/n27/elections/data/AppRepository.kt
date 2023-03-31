package com.n27.elections.data

import android.content.SharedPreferences
import com.n27.core.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private var sharedPreferences: SharedPreferences) {

    internal suspend fun isFirstLaunch() = withContext(Dispatchers.IO) {
        !sharedPreferences.contains(Constants.LAUNCHED)
    }

    internal suspend fun saveFirstLaunch() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().putBoolean(Constants.LAUNCHED, true).apply()
    }
}
