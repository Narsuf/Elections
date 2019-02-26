package com.jorgedguezm.elections.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.data.Party

import io.reactivex.Single

@Dao
interface PartiesDao {

    @Query("SELECT * FROM party")
    fun queryParties(): Single<List<Party>>

    @Query("SELECT * FROM party WHERE name = :name LIMIT 1")
    fun getParty(name: String): Single<Party>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParty(party: Party)
}