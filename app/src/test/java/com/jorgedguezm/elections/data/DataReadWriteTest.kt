package com.jorgedguezm.elections.data

import androidx.room.Room

import com.jorgedguezm.elections.data.DataUtils.Companion.generateElection
import com.jorgedguezm.elections.room.Database
import com.jorgedguezm.elections.room.ElectionDao

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain

@RunWith(RobolectricTestRunner::class)
class DataReadWriteTest {

    private lateinit var electionDao: ElectionDao
    private lateinit var db: Database

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

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
    fun writeElectionAndRead() = runBlockingTest {
        val election = generateElection()

        electionDao.insertElection(election)

        val elections = electionDao.queryChamberElections(election.place, election.chamberName)
        val dbElection = electionDao.getElection(election.id)

        assertTrue(elections.contains(election))
        assertEquals(dbElection, election)
    }
}