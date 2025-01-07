package com.example.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.sleeptracker.database.SleepDatabaseDao
import com.example.sleeptracker.database.SleepNight
import com.example.sleeptracker.formatNights
import kotlinx.coroutines.launch


class SleepTrackerViewModel(private val dataSource: SleepDatabaseDao, application: Application): AndroidViewModel(application) {

    val sleepNightsList = dataSource.getAllSleepNights()
    val sleepNightsDataString = sleepNightsList.map {
        formatNights(it, application.resources)
    }

    private val _eventStopSleepTracking = MutableLiveData<Boolean>()
    val eventStopSleepTracking: LiveData<Boolean> get() = _eventStopSleepTracking

    private val activeSleepNight = MutableLiveData<SleepNight?>()

    val startButtonVisible = activeSleepNight.map {
        null == it
    }
    val stopButtonVisible = activeSleepNight.map {
        null != it
    }
    val clearButtonVisible = sleepNightsList.map {
        it.isNotEmpty()
    }

    init {
        viewModelScope.launch {
            activeSleepNight.value = getActiveSleepNight()
        }
        _eventStopSleepTracking.value = false
    }

    private suspend fun getActiveSleepNight(): SleepNight? {
        var night = dataSource.getTonightSleepData()
        if (night?.endTimeInMillis != night?.startTimeInMillis) {
            night = null
        }
        return night
    }

    fun onStartTracking() {
        viewModelScope.launch {
            val sleepNight = SleepNight()
            dataSource.insert(sleepNight)
            activeSleepNight.value = getActiveSleepNight()
        }
    }

    fun onStopTracking() {
        viewModelScope.launch {
            val sleepNight = activeSleepNight.value ?: return@launch
            sleepNight.endTimeInMillis = System.currentTimeMillis()
            dataSource.update(sleepNight)
            _eventStopSleepTracking.value = true
        }
    }

    fun onStopTrackingComplete() {
        _eventStopSleepTracking.value = false
    }

    fun onClearClick() {
        viewModelScope.launch {
            dataSource.clear()
            activeSleepNight.value = null
        }
    }
}