package com.n27.elections.data

import com.google.firebase.database.FirebaseDatabase
import com.n27.core.Constants.EMPTY_RESPONSE_FROM_FIREBASE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.data.remote.firebase.toElections
import com.n27.core.domain.election.Election
import com.n27.core.domain.election.Elections
import com.n27.elections.domain.repositories.ElectionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class ElectionRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val dao: ElectionDao,
    private val utils: DataUtils
) : ElectionRepository {

    override suspend fun getElectionsLocally(): Elections? = dao.getElections()
        .takeIf { it.isNotEmpty() }
        ?.toElections()

    override suspend fun getElectionsRemotely(): Result<Elections> = if (utils.isConnectedToInternet()) {
        firebaseDatabase
            .getReference("elections").get()
            .await()
            .toElections()
            ?.let { success(Elections(it)) } ?: failure(Throwable(EMPTY_RESPONSE_FROM_FIREBASE))
    } else {
        failure(Throwable(NO_INTERNET_CONNECTION))
    }

    override suspend fun saveElections(elections: List<Election>) {
        dao.insertElections(elections.toElectionsWithResultsAndParty())
    }
}
