package com.n27.elections.domain.repositories

interface AppRepository {

    fun isFirstLaunch(): Boolean
    fun saveFirstLaunch()
}