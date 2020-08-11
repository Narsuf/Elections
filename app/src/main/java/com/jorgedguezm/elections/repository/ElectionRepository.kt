package com.jorgedguezm.elections.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.api.ApiResponse
import com.jorgedguezm.elections.models.entities.Election
import com.jorgedguezm.elections.room.ElectionsDao
import com.jorgedguezm.elections.models.Resource
import com.jorgedguezm.elections.models.network.ElectionApiResponse

import timber.log.Timber

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject
constructor(val service: ApiInterface, val dao: ElectionsDao) : Repository {

    init {
        Timber.d("Inject UserRepo")
    }

    fun loadElections(place: String, chamber: String?): LiveData<Resource<List<Election>>> {
        return object : NetworkBoundRepository<List<Election>, ElectionApiResponse>() {
            override fun loadFromDb(): LiveData<List<Election>> {
                val elections = if (chamber != null)
                    dao.queryChamberElections(place, chamber)
                else
                    dao.queryElections(place)

                val data: MutableLiveData<List<Election>> = MutableLiveData()
                data.postValue(elections)
                return data
            }

            override fun shouldFetch(data: List<Election>?): Boolean {
                return data.isNullOrEmpty()
            }

            override fun fetchService(): LiveData<ApiResponse<ElectionApiResponse>> {
                return if (chamber != null)
                    service.getChamberElections(place, chamber)
                else
                    service.getElections(place)
            }

            override fun onFetchFailed(message: String?) {
                Timber.e("onFetchFailed: $message")
            }

            override fun saveFetchData(items: ElectionApiResponse) {
                dao.insertElections(items.data)
            }
        }.asLiveData()
    }
}