package com.jorgedguezm.elections.data

import androidx.room.Room

import com.jorgedguezm.elections.data.DataUtils.Companion.generateElection
import com.jorgedguezm.elections.data.DataUtils.Companion.generateParty
import com.jorgedguezm.elections.data.DataUtils.Companion.generateResults
import com.jorgedguezm.elections.data.source.local.Database
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.local.PartiesDao
import com.jorgedguezm.elections.data.source.local.ResultsDao

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
    private lateinit var resultsDao: ResultsDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = RuntimeEnvironment.systemContext
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
                .allowMainThreadQueries()
                .build()
        partiesDao = db.partiesDao()
        electionsDao = db.electionsDao()
        resultsDao = db.resultsDao()
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

    @Test
    @Throws(Exception::class)
    fun writeResultsAndRead() {
        val party = generateParty()
        val election = generateElection()
        val result = generateResults(party, election)
        electionsDao.insertElection(election)
        resultsDao.insertResults(result)
        resultsDao.getElectionResults(election.year, election.place, election.chamberName).test()
                .assertValue { results ->
                    results.contains(result.apply {
                        // For some reason room is saving the foreign key as the primary key too
                        id = results[0].id
                    })
                }
    }
}