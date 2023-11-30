package com.n27.elections.data

import com.n27.core.Constants.EMPTY_RESPONSE_FROM_FIREBASE
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.data.remote.firebase.FirebaseApi
import com.n27.core.data.remote.firebase.mappers.toElections
import com.n27.core.domain.election.Election
import com.n27.core.domain.election.Elections
import com.n27.elections.domain.repositories.ElectionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepositoryImpl @Inject constructor(
    private val firebaseApi: FirebaseApi,
    private val dao: ElectionDao,
) : ElectionRepository {

    override suspend fun getElectionsLocally(): Elections? = dao.getElections()
        .takeIf { it.isNotEmpty() }
        ?.toElections()

    override suspend fun getElectionsRemotely(): Result<Elections> = firebaseApi.get("elections")
        .first()
        .mapCatching { dataSnapshot ->
            dataSnapshot.toElections()
                ?.let { Elections(it) }
                ?: throw Throwable(EMPTY_RESPONSE_FROM_FIREBASE)
        }


    override suspend fun saveElections(elections: List<Election>) {
        dao.insertElections(elections.toElectionsWithResultsAndParty())
    }
}
