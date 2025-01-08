package com.example.sleeptracker.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleeptracker.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(private val sleepNightKey: Long, val database: SleepDatabaseDao): ViewModel() {

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?> get() = _navigateToSleepTracker


    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            val tonight = database.getSleepNightForId(sleepNightKey) ?: return@launch
            tonight.sleepQuality = quality
            database.update(tonight)
            _navigateToSleepTracker.value = true
        }
    }

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }
}