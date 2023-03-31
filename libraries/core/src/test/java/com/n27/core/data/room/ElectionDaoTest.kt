package com.n27.core.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.n27.core.data.local.room.Database
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElection
import com.n27.core.data.local.room.mappers.toElectionWithResultsAndParty
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateElections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElectionDaoTest {

    private lateinit var electionDao: ElectionDao
    private lateinit var db: Database

    @ExperimentalCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider
                .getApplicationContext(), Database::class.java)
                .allowMainThreadQueries()
                .build()

        electionDao = db.electionDao()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun closeDb() { db.close() }

    @ExperimentalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun writeElectionsAndRead() = runTest {
        generateElections()
            .map { it.toElectionWithResultsAndParty() }
            .let { electionDao.insertElectionsWithResultsAndParty(it) }

        val dbElections = electionDao.getElections().map { it.toElection().sortResultsByElectsAndVotes() }

        dbElections.forEach { election ->
            val dbElection = electionDao.getElection(election.id)
                .toElection()
                .sortResultsByElectsAndVotes()

            assertTrue(dbElections.contains(election))
            assertEquals(dbElection, election)
        }
    }
}
