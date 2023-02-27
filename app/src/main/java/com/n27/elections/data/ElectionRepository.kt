package com.n27.elections.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.n27.elections.data.models.Election
import com.n27.elections.data.room.ElectionDao
import com.n27.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.success
import kotlin.coroutines.suspendCoroutine

@Singleton
class ElectionRepository @Inject constructor(
    private var service: ElectionApi,
    private var dao: ElectionDao,
    private var dataUtils: DataUtils,
    private var firebaseDatabase: FirebaseDatabase,
    private var crashlytics: FirebaseCrashlytics
) {

    internal suspend fun getElections() = if (dataUtils.isConnectedToInternet())
        getElectionsRemotely()
    else
        getElectionsFromDb().takeIf { it.isNotEmpty() } ?: throw Throwable(NO_INTERNET_CONNECTION)

    private suspend fun getElectionsRemotely() = runCatching {
        withContext(Dispatchers.IO) {
            service.getElections().elections.apply { insertInDb() }
        }
    }.getOrElse {
        crashlytics.recordException(Exception("Main service not responding"))
        getElectionsFromDb()
            .takeIf { it.isNotEmpty() } ?: getElectionsFromFirebase().apply { insertInDb() }
    }

    private suspend fun getElectionsFromDb() = withContext(Dispatchers.IO) { dao.getElections() }
        .toElections()

    private suspend fun getElectionsFromFirebase() = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            firebaseDatabase.getReference("elections").get()
                .addOnSuccessListener { continuation.resumeWith(success(it)) }
                .addOnFailureListener { throw it }
        }
    }.toElections() ?: throw Throwable("Empty response from Firebase")

    private suspend fun List<Election>.insertInDb() {
        forEach { dao.insertElectionWithResultsAndParty(it.toElectionWithResultsAndParty()) }
    }
}
