package com.n27.elections.data

import android.content.SharedPreferences
import com.n27.elections.Constants.LAUNCHED
import com.n27.elections.domain.repositories.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : AppRepository {

    override fun isFirstLaunch(): Boolean = !sharedPreferences.contains(LAUNCHED)

    override fun saveFirstLaunch() { sharedPreferences.edit().putBoolean(LAUNCHED, true).apply() }
}
