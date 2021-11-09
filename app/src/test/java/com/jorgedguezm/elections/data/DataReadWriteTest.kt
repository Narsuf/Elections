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

@RunWith(RobolectricTestRunner::class)
class DataReadWriteTest {

    private lateinit var electionDao: ElectionDao
    private lateinit var db: Database

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider
                .getApplicationContext(), Database::class.java)
                .allowMainThreadQueries()
                .build()

        electionDao = db.electionsDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeElectionAndRead() {
        val election = generateElection()

        electionDao.insertElection(election).test()

        electionDao.queryChamberElections(election.place, election.chamberName).test()
            .assertValue { it.contains(election) }

        electionDao.getElection(election.id).test().assertValue { it == election }
    }
}