package com.jorgedguezm.elections.data

import androidx.room.Room

import com.jorgedguezm.elections.data.DataUtils.Companion.generateElection
import com.jorgedguezm.elections.room.Database
import com.jorgedguezm.elections.room.ElectionsDao

import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class DataReadWriteTest {

    private lateinit var electionsDao: ElectionsDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = RuntimeEnvironment.systemContext
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
                .allowMainThreadQueries()
                .build()
        electionsDao = db.electionsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeElectionAndRead() {
        val election = generateElection()

        electionsDao.insertElection(election)

        assertTrue(electionsDao.queryChamberElections(election.place, election.chamberName)
                .contains(election))

        assertTrue(electionsDao.getElection(election.id) == election)
    }
}