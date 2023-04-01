package com.n27.core.data.local.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.n27.core.data.local.room.mappers.toElection
import com.n27.core.data.local.room.mappers.toElections
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateElections
import junit.framework.TestCase.assertEquals
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
        val elections = generateElections()
        electionDao.insertElectionsWithResultsAndParty(elections.toElectionsWithResultsAndParty())
        val dbElections = electionDao.getElections().toElections()

        assertEquals(elections, dbElections)

        dbElections.forEach { electionRaw ->
            val election = elections.first { it.id == electionRaw.id }
            val dbElection = electionDao.getElection(electionRaw.id).toElection()

            assertEquals(dbElection, electionRaw)
            assertEquals(dbElection, election)
        }
    }
}
