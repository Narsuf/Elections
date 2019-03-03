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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParty(party: Party)
}