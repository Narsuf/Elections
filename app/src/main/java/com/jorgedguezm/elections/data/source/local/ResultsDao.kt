package com.jorgedguezm.elections.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.data.Results

import io.reactivex.Single

@Dao
interface ResultsDao {

    @Query("SELECT * FROM results r INNER JOIN election e ON e.id = r.electionId " +
            "WHERE e.year = :year AND e.place = :place AND e.chamberName = :chamberName")
    fun getElectionResults(year: Int, place: String, chamberName: String): Single<List<Results>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResults(results: Results)
}