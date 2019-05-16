package com.jorgedguezm.elections

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room

import com.jorgedguezm.elections.data.source.local.Database
import com.jorgedguezm.elections.data.source.local.PartiesDao

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ElectionEntityReadWriteTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var partiesDao: PartiesDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = RuntimeEnvironment.systemContext
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
                .allowMainThreadQueries()
                .build()
        partiesDao = db.partiesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePartyAndRead() {
        val myParty = ElectionUtils.generateParty()
        partiesDao.insertParty(myParty)
        partiesDao.queryParties().test().assertValue { parties ->
            parties.contains(myParty)
        }
    }
}