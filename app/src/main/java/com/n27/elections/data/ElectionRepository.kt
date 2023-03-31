package com.n27.elections.data

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.remote.firebase.toElections
import com.n27.core.data.models.Election
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElectionWithResultsAndParty
import com.n27.core.data.local.room.mappers.toElections
import com.n27.elections.data.api.ElectionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.success
import kotlin.coroutines.suspendCoroutine

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

    private suspend fun getElectionsRemotely() = runCatching {
        getElectionsFromApi().apply { insertInDb() }
    }.getOrElse {
        Firebase.crashlytics.recordException(Exception("Main service not responding"))
        getElectionsFromDb().takeIf { it.isNotEmpty() } ?: getElectionsFromFirebase().apply { insertInDb() }
    }

    private suspend fun getElectionsFromApi() = withContext(Dispatchers.IO) { service.getElections() }.elections

    private suspend fun getElectionsFromDb() = withContext(Dispatchers.IO) { dao.getElections() }.toElections()

    private suspend fun getElectionsFromFirebase() = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            firebaseDatabase.getReference("elections").get()
                .addOnSuccessListener { continuation.resumeWith(success(it)) }
                .addOnFailureListener { throw it }
        }
    }.toElections() ?: throw Throwable("Empty response from Firebase")

    private suspend fun List<Election>.insertInDb() {
        val elections = map { it.toElectionWithResultsAndParty() }
        withContext(Dispatchers.IO) { dao.insertElectionsWithResultsAndParty(elections) }
    }
}
