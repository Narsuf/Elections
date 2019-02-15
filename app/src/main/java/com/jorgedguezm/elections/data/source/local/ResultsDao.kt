package com.jorgedguezm.elections.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.data.Results

import io.reactivex.Single

@Dao
interface ResultsDao {

    @Query("SELECT * FROM results WHERE partyId = :partyId AND electionId = :electionId " +
            "LIMIT 1")
    fun getResults(partyId: String, electionId: Long): Single<Results>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResults(results: Results)
}