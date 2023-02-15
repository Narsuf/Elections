package com.jorgedguezm.elections.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.ElectionDao
import com.jorgedguezm.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import com.jorgedguezm.elections.presentation.common.Constants.SERVER_COMMUNICATION_ISSUES
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class ElectionRepository @Inject constructor(
    internal var service: ElectionApi,
    internal var dao: ElectionDao,
    internal var dataUtils: DataUtils,
    internal var firebaseDatabase: FirebaseDatabase,
    internal var crashlytics: FirebaseCrashlytics
) {

    internal suspend fun getElections() = if (dataUtils.isConnectedToInternet())
        getElectionsFromApi()
    else
        flowOf(getElectionsFromDb(offline = true))


    private suspend fun getElectionsFromApi() = flow {
        val elections = service.getElections().elections

        dao.insertElectionsWithResultsAndParty(elections.map { it.toElectionWithResultsAndParty() })

        emit(success(elections))
    }.catch { throwable ->
        throwable.message?.takeIf { it.contains("Failed to connect to ") }?.let {
            crashlytics.recordException(Exception("Main service down"))
            getElectionsFromDb()
                .takeIf { it.isSuccess }?.let { emit(it) }
                ?: getElectionsFromFirebase().collect { emit(it) }
        } ?: emit(throwable.getResult())
    }

    private fun Throwable.getResult(): Result<List<Election>> {
        crashlytics.recordException(this)
        return failure(this)
    }

    private suspend fun getElectionsFromDb(offline: Boolean = false): Result<List<Election>> {
        val elections = dao.getElections()

        return if (elections.isNotEmpty()) {
            success(elections.map { it.toElection() })
        } else {
            failure(
                if (offline)
                    Throwable(NO_INTERNET_CONNECTION)
                else
                    Throwable(SERVER_COMMUNICATION_ISSUES)
            )
        }
    }

    private fun getElectionsFromFirebase(): Flow<Result<List<Election>>> = channelFlow {
        firebaseDatabase.getReference("elections").get().addOnSuccessListener { dataSnapshot ->
            launch {
                val gti = object : GenericTypeIndicator<List<Election>>() { }
                dataSnapshot.getValue(gti)?.let { elections ->
                    dao.insertElectionsWithResultsAndParty(
                        elections.map { it.toElectionWithResultsAndParty() }
                    )
                    send(success(elections))
                }
            }
        }.addOnFailureListener {
            launch { send(failure(it)) }
        }

        awaitClose()
    }
}
