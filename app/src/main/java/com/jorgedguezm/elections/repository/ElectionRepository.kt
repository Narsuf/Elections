package com.jorgedguezm.elections.repository

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.room.ElectionDao
import com.jorgedguezm.elections.utils.Utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ElectionRepository @Inject constructor(
    internal var service: ApiInterface,
    internal var dao: ElectionDao,
    override var utils: Utils
) : Repository<List<Election>>(utils) {

    open suspend fun loadElections(place: String?, chamber: String?): List<Election> {
        return loadData(listOf(place, chamber))
    }

    override suspend fun getDataFromApi(filterParams: List<String?>): List<Election> {
        return service.getElections(filterParams[0], filterParams[1]).data
    }

    override suspend fun insert(value: List<Election>) {
        dao.insertElections(value)
    }

    override suspend fun getDataFromDb(filterParams: List<String?>): List<Election> {
        return dao.queryElections(filterParams[0], filterParams[1])
    }
}