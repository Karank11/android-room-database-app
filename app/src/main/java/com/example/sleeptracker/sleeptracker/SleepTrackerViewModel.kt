package com.example.sleeptracker.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.sleeptracker.database.SleepDatabaseDao
import com.example.sleeptracker.database.SleepNight
import com.example.sleeptracker.formatNights
import kotlinx.coroutines.launch


class SleepTrackerViewModel(private val dataSource: SleepDatabaseDao, application: Application): AndroidViewModel(application) {

    private val sleepNightsList = dataSource.getAllSleepNights()
    val sleepNightsDataString = sleepNightsList.map {
        formatNights(it, application.resources)
    }

    private val currentSleepNight = MutableLiveData<SleepNight?>()
    init {
        viewModelScope.launch {
            currentSleepNight.value = dataSource.getTonightSleepData()
        }
    }

    fun onStartTracking() {
        viewModelScope.launch {

            Log.i("SleepTracking", "sleepID = ${currentSleepNight.value?.nightId} ----> current.endtime = ${currentSleepNight.value?.endTimeInMillis} && systemTime = ${System.currentTimeMillis()}")
            if (currentSleepNight.value != null && currentSleepNight.value?.endTimeInMillis == currentSleepNight.value?.startTimeInMillis) {
                Log.i("SleepTracking", "One SleepTracker is already running!")
                return@launch
            }
            val sleepNight = SleepNight()
            dataSource.insert(sleepNight)
            currentSleepNight.value = dataSource.getTonightSleepData()
        }
    }


    fun onStopTracking() {
        viewModelScope.launch {
            val lastSleepNight = currentSleepNight.value
            if (lastSleepNight == null || lastSleepNight.startTimeInMillis != lastSleepNight.endTimeInMillis) {
                Log.i("SleepTracking", "No Active SleepTracker running !")
                return@launch
            }
            lastSleepNight.endTimeInMillis = System.currentTimeMillis()
            dataSource.update(lastSleepNight)
        }
    }

    fun onClearClick() {
        viewModelScope.launch {
            dataSource.clear()
            currentSleepNight.value = null
        }
    }
}