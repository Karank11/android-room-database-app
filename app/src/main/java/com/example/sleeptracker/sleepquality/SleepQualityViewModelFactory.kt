package com.example.sleeptracker.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptracker.database.SleepDatabaseDao

class SleepQualityViewModelFactory(private val sleepNightKey: Long, val database: SleepDatabaseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)) {
            return SleepQualityViewModel(sleepNightKey, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}