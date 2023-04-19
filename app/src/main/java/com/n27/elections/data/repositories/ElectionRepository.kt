package com.n27.elections.data.repositories

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.data.models.Election
import com.n27.core.data.remote.firebase.toElections
import com.n27.elections.data.api.ElectionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(
    private val service: ElectionApi,
    private val dao: ElectionDao,
    private val firebaseDatabase: FirebaseDatabase,
    private val utils: DataUtils
) {

    internal suspend fun getElections() = if (utils.isConnectedToInternet())
        getElectionsRemotely()
    else
        getElectionsFromDb() ?: throw Throwable(NO_INTERNET_CONNECTION)

    private suspend fun getElectionsRemotely() = runCatching { getElectionsFromApi() }
        .getOrElse {
            Firebase.crashlytics.recordException(Exception("Main service not responding"))
            getElectionsFromDb() ?: getElectionsFromFirebase()
        }

    private suspend fun getElectionsFromApi() = withContext(Dispatchers.IO) { service.getElections() }
        .elections
        .apply { insertInDb() }

    private suspend fun getElectionsFromDb() = withContext(Dispatchers.IO) { dao.getElections() }
        .toElections()
        .takeIf { it.isNotEmpty() }

    private suspend fun getElectionsFromFirebase() =
        withContext(Dispatchers.IO) { firebaseDatabase.getReference("elections").get().await() }
            .run { toElections() ?: throw Throwable("Empty response from Firebase") }
            .apply { insertInDb() }

    private suspend fun List<Election>.insertInDb() {
        withContext(Dispatchers.IO) { dao.insertElections(toElectionsWithResultsAndParty()) }
    }
}
