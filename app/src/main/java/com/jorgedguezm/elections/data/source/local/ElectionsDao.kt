package com.jorgedguezm.elections.data.source.local

import androidx.room.*

import com.jorgedguezm.elections.data.Election

import io.reactivex.Single

@Dao
interface ElectionsDao {

    @Query("SELECT * FROM election")
    fun queryElections(): Single<List<Election>>

    @Query("SELECT * FROM election WHERE id = :id LIMIT 1")
    fun getElection(id: Long): Single<Election>

    @Query("DELETE FROM election")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)
}