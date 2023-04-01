package com.n27.elections.data.repositories

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElectionWithResultsAndParty
import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.models.Election
import com.n27.core.data.remote.firebase.toElections
import com.n27.elections.data.api.ElectionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(
    private val service: ElectionApi,
    private val dao: ElectionDao,
    private val firebaseDatabase: FirebaseDatabase,
    private val dataUtils: DataUtils
) {

    internal suspend fun getElections() = if (dataUtils.isConnectedToInternet())
        getElectionsRemotely()
    else
        getElectionsFromDb().takeIf { it.isNotEmpty() } ?: throw Throwable(NO_INTERNET_CONNECTION)

    private suspend fun getElectionsRemotely() = runCatching { getElectionsFromApi() }
        .getOrElse {
            Firebase.crashlytics.recordException(Exception("Main service not responding"))
            getElectionsFromDb().takeIf { it.isNotEmpty() } ?: getElectionsFromFirebase()
        }

    private suspend fun getElectionsFromApi() = flow { emit(service.getElections()) }
        .flowOn(Dispatchers.IO)
        .map { it.elections }
        .onEach { it.insertInDb() }
        .first()

    private suspend fun getElectionsFromDb() = withContext(Dispatchers.IO) { dao.getElections() }.toElections()

    private suspend fun getElectionsFromFirebase() =
        flow { emit(firebaseDatabase.getReference("elections").get().await()) }
            .flowOn(Dispatchers.IO)
            .map { it.toElections() ?: throw Throwable("Empty response from Firebase") }
            .onEach { it.insertInDb() }
            .first()

    private suspend fun List<Election>.insertInDb() {
        val elections = map { it.toElectionWithResultsAndParty() }
        withContext(Dispatchers.IO) { dao.insertElectionsWithResultsAndParty(elections) }
    }
}
