package com.jorgedguezm.elections.data

import androidx.room.Room

import com.jorgedguezm.elections.data.DataUtils.Companion.generateElection
import com.jorgedguezm.elections.data.DataUtils.Companion.generateParty
import com.jorgedguezm.elections.data.source.local.Database
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.local.PartiesDao

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class DataReadWriteTest {

    private lateinit var partiesDao: PartiesDao
    private lateinit var electionsDao: ElectionsDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = RuntimeEnvironment.systemContext
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
                .allowMainThreadQueries()
                .build()
        partiesDao = db.partiesDao()
        electionsDao = db.electionsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePartyAndRead() {
        val myParty = generateParty()
        partiesDao.insertParty(myParty)
        partiesDao.queryParties().test().assertValue { parties ->
            parties.contains(myParty)
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeElectionAndRead() {
        val election = generateElection()
        electionsDao.insertElection(election)
        electionsDao.queryElections().test().assertValue { elections ->
            elections.contains(election)
        }
    }
}