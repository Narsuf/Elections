package com.n27.core.data.local.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.n27.core.data.local.room.mappers.toElection
import com.n27.core.data.local.room.mappers.toElectionList
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.data.local.room.mappers.toPartyRaw
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateElections
import com.n27.test.generators.ElectionRandomGenerator.Companion.generateParties
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElectionDaoTest {

    private lateinit var electionDao: ElectionDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider
                .getApplicationContext(), Database::class.java)
                .allowMainThreadQueries()
                .build()

        electionDao = db.electionDao()
    }

    @After
    fun closeDb() { db.close() }

    @Test
    fun writeElectionsAndRead() = runBlocking {
        val elections = generateElections()
        electionDao.insertElections(elections.toElectionsWithResultsAndParty())
        val dbElections = electionDao.getElections()
            .toElectionList()
            .map { it.sortResultsByElectsAndVotes() }
            .sortByDateAndFormat()

        assertEquals(elections, dbElections)

        dbElections.forEach { electionRaw ->
            val election = elections.first { it.id == electionRaw.id }
            val dbElection = electionDao.getElection(electionRaw.id)
                .toElection()
                .sortResultsByElectsAndVotes()

            assertEquals(dbElection, electionRaw)
            assertEquals(dbElection, election)
        }
    }

    @Test
    fun getParties() = runBlocking {
        val parties = generateParties().map { it.toPartyRaw() }.sortedBy { it.partyId }
        parties.forEach { electionDao.insertParty(it) }

        assertEquals(parties, electionDao.getParties())
    }
}
