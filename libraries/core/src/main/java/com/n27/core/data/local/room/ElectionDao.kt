package com.n27.core.data.local.room

import androidx.room.*
import com.n27.core.data.local.room.models.ElectionRaw
import com.n27.core.data.local.room.models.ElectionWithResultsAndParty
import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.local.room.models.ResultRaw

@Dao
interface ElectionDao {

    @Query("SELECT * FROM elections")
    suspend fun getElections(): List<ElectionWithResultsAndParty>

    @Query("SELECT * FROM elections WHERE electionId = :id")
    suspend fun getElection(id: Long): ElectionWithResultsAndParty

    @Query("SELECT * FROM parties")
    suspend fun getParties(): List<PartyRaw>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: ElectionRaw)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParty(election: PartyRaw)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(election: ResultRaw)

    @Transaction
    suspend fun insertElectionsWithResultsAndParty(elections: List<ElectionWithResultsAndParty>) {
        elections.forEach { insertElectionWithResultsAndParty(it) }
    }

    private suspend fun insertElectionWithResultsAndParty(electionRaw: ElectionWithResultsAndParty) {
        val election = electionRaw.election
        insertElection(electionRaw.election)

        electionRaw.results.forEach {
            val party = it.party
            insertParty(party)

            val result = it.result.apply {
                resultPartyId = party.partyId
                resultElectionId = election.electionId
            }
            insertResult(result)
        }
    }
}
