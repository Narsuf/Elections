package com.n27.elections.data

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.n27.elections.data.models.Election
import com.n27.elections.data.room.ElectionDao
import com.n27.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.success
import kotlin.collections.forEach
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
        getElectionsRemotely().onEach { it.insertInDb() }
    else
        getFlowElectionsFromDb()

    private suspend fun getElectionsRemotely() = flow { emit(service.getElections().elections) }
        .flowOn(Dispatchers.IO)
        .catch { throwable ->
            throwable.message?.takeIf { it.contains("Failed to connect to ") }?.let {
                crashlytics.recordException(Exception("Main service down"))
                getElectionsFromDb()
                    .takeIf { it.isNotEmpty() }?.let { emit(it.toElections()) }
                    ?: emit(getElectionsFromFirebase())
            } ?: throw throwable
        }

    private suspend fun getElectionsFromDb() = withContext(Dispatchers.IO) { dao.getElections() }

    private suspend fun getFlowElectionsFromDb() = flow { emit(dao.getElections()) }
            .flowOn(Dispatchers.IO)
            .map { it.toElections() }
            .onEach { if (it.isEmpty()) throw Throwable(NO_INTERNET_CONNECTION) }

    private suspend fun getElectionsFromFirebase() = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val onSuccessListener = OnSuccessListener<DataSnapshot> { dataSnapshot ->
                val gti = object : GenericTypeIndicator<List<Election>>() {}
                dataSnapshot.getValue(gti)?.let { elections ->
                    continuation.resumeWith(success(elections))
                } ?: throw Throwable("Response from Firebase was empty")
            }

            firebaseDatabase.getReference("elections").get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener { throw it }
        }
    }

    private suspend fun List<Election>.insertInDb() {
        forEach { dao.insertElectionWithResultsAndParty(it.toElectionWithResultsAndParty()) }
    }
}
