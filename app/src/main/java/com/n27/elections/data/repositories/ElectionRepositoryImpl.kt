package com.n27.elections.data.repositories

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.domain.models.Election
import com.n27.core.data.remote.firebase.toElections
import com.n27.core.domain.ElectionRepository
import com.n27.core.domain.models.Elections
import com.n27.elections.data.api.ElectionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class ElectionRepositoryImpl @Inject constructor(
    private val api: ElectionApi,
    private val dao: ElectionDao,
    private val firebaseDatabase: FirebaseDatabase,
    private val utils: DataUtils
): ElectionRepository {

    override suspend fun getElections(): Result<Elections> = if (utils.isConnectedToInternet())
        getElectionsRemotely()
    else
        getElectionsFromDb() ?: failure(Throwable(NO_INTERNET_CONNECTION))

    private suspend fun getElectionsRemotely(): Result<Elections> = runCatching { getElectionsFromApi() }
        .getOrElse {
            Firebase.crashlytics.recordException(Exception("Main service not responding"))
            getElectionsFromDb() ?: getElectionsFromFirebase()
        }

    private suspend fun getElectionsFromApi(): Result<Elections> = withContext(Dispatchers.IO) { api.getElections() }
        .elections
        .apply { insertInDb() }
        .let { success(Elections(it)) }

    private suspend fun getElectionsFromDb(): Result<Elections>? = withContext(Dispatchers.IO) { dao.getElections() }
        .takeIf { it.isNotEmpty() }
        ?.run { success(toElections()) }

    private suspend fun getElectionsFromFirebase(): Result<Elections> =
        withContext(Dispatchers.IO) { firebaseDatabase.getReference("elections").get().await() }
            .toElections()
            ?.let {
                it.insertInDb()
                success(Elections(it))
            }
            ?: failure(Throwable("Empty response from Firebase"))

    private suspend fun List<Election>.insertInDb() {
        withContext(Dispatchers.IO) { dao.insertElections(toElectionsWithResultsAndParty()) }
    }
}
