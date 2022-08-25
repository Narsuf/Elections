package com.jorgedguezm.elections.data.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.jorgedguezm.elections.data.models.ElectionGenerator.Companion.generateElection
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
/*
@RunWith(RobolectricTestRunner::class)
class ElectionDaoTest {

    private lateinit var electionDao: ElectionDao
    private lateinit var db: Database

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun closeDb() {
        db.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun writeElectionAndRead() = runTest {
        val election = generateElection()

        electionDao.insertElection(election)

        val elections = electionDao.queryElections(election.place, election.chamberName)
        val dbElection = electionDao.getElection(election.id)

        assertTrue(elections.contains(election))
        assertEquals(dbElection, election)
    }
}*/
