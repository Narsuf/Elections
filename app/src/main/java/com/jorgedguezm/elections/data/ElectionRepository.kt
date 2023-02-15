package com.jorgedguezm.elections.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.ElectionDao
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(
    internal var service: ElectionApi,
    internal var dao: ElectionDao,
    internal var dataUtils: DataUtils,
    internal var firebaseDatabase: FirebaseDatabase
) {

    internal suspend fun getElections(fallback: Boolean = false): Flow<List<Election>> {
        return if (dataUtils.isConnectedToInternet()) {
            if (!fallback)
                getElectionsFromApi()
            else
                getElectionsFromDb().takeIf { !isDatabaseEmpty() } ?: getElectionsFromFirebase()
        } else {
            getElectionsFromDb()
        }
    }

    private suspend fun getElectionsFromApi() = channelFlow {
        val elections = service.getElections().elections

        dao.insertElectionsWithResultsAndParty(
            elections.map { it.toElectionWithResultsAndParty() }
        )

        send(elections)
    }

    private suspend fun getElectionsFromDb() = channelFlow {
        send(dao.getElections().map { it.toElection() })
    }

    private suspend fun isDatabaseEmpty() = dao.getElections().isEmpty()

    private fun getElectionsFromFirebase(): Flow<List<Election>> = channelFlow {
        firebaseDatabase.getReference("elections").get().addOnSuccessListener { dataSnapshot ->
            launch {
                val gti = object : GenericTypeIndicator<List<Election>>() { }
                dataSnapshot.getValue(gti)?.let { elections ->
                    dao.insertElectionsWithResultsAndParty(
                        elections.map { it.toElectionWithResultsAndParty() }
                    )
                    send(elections)
                }
            }
        }

        awaitClose()
    }
}
