package com.example.sleeptracker

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sleeptracker.database.SleepDatabase
import com.example.sleeptracker.database.SleepDatabaseDao
import com.example.sleeptracker.database.SleepNight
import junit.framework.TestCase.assertEquals
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.io.IOException
import kotlin.jvm.Throws

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDatabaseDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        sleepDatabaseDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTonightSleepData() {
        val sleepNight = SleepNight()
        sleepDatabaseDao.insert(sleepNight)
        val tonight = sleepDatabaseDao.getTonightSleepData()
        assertEquals(tonight?.sleepQuality, -1)
    }
}