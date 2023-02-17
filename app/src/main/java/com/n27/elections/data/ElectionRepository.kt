package com.n27.elections.data

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.n27.elections.data.models.Election
import com.n27.elections.data.room.ElectionDao
import com.n27.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.presentation.common.Constants.SERVER_COMMUNICATION_ISSUES
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.coroutines.suspendCoroutine

@Singleton
class ElectionRepository @Inject constructor(
    internal var service: ElectionApi,
    internal var dao: ElectionDao,
    internal var dataUtils: DataUtils,
    internal var firebaseDatabase: FirebaseDatabase,
    internal var crashlytics: FirebaseCrashlytics
) {

    internal suspend fun getElections() = if (dataUtils.isConnectedToInternet())
        getElectionsRemotely().onSuccess { it.insertInDb() }
    else
        getElectionsFromDb()

    private suspend fun getElectionsRemotely() = runCatching {
        success(service.getElections().elections)
    }.getOrElse { throwable ->
        throwable.message?.takeIf { it.contains("Failed to connect to ") }?.let {
            crashlytics.recordException(Exception("Main service down"))
            getElectionsFromDb()
                .onSuccess { success(it) }
                .onFailure { success(getElectionsFromFirebase()) }
        } ?: failureException(throwable)
    }

    private fun failureException(t: Throwable): Result<List<Election>> {
        crashlytics.recordException(t)
        return failure(Throwable(SERVER_COMMUNICATION_ISSUES))
    }

    private suspend fun getElectionsFromDb(): Result<List<Election>> {
        val elections = dao.getElections()

        return if (elections.isNotEmpty())
            success(elections.map { it.toElection() })
        else
            failure(Throwable(NO_INTERNET_CONNECTION))
    }

    private suspend fun getElectionsFromFirebase() = suspendCoroutine { continuation ->
        val onSuccessListener = OnSuccessListener<DataSnapshot> { dataSnapshot ->
            val gti = object : GenericTypeIndicator<List<Election>>() { }
            dataSnapshot.getValue(gti)?.let { elections ->
                continuation.resumeWith(success(elections))
            } ?: continuation.resumeWith(failureException(Throwable("Response from Firebase was empty")))
        }

        firebaseDatabase.getReference("elections").get()
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener { continuation.resumeWith(failureException(it)) }
    }

    private suspend fun List<Election>.insertInDb() {
        forEach { dao.insertElectionWithResultsAndParty(it.toElectionWithResultsAndParty()) }
    }
}
