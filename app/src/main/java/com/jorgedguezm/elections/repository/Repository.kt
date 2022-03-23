package com.jorgedguezm.elections.repository

import com.jorgedguezm.elections.utils.Utils

abstract class Repository<T>(internal open var utils: Utils) {

    suspend fun loadData(filterParams: List<String?>): T {
        return if (utils.isConnectedToInternet()) {
            val data = getDataFromApi(filterParams)
            insert(data)
            data
        } else {
            getDataFromDb(filterParams)
        }
    }

    abstract suspend fun getDataFromApi(filterParams: List<String?>): T
    abstract suspend fun insert(value: T)
    abstract suspend fun getDataFromDb(filterParams: List<String?>): T
}