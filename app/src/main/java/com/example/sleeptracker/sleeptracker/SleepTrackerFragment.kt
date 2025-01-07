package com.example.sleeptracker.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sleeptracker.R
import com.example.sleeptracker.database.SleepDatabase
import com.example.sleeptracker.databinding.FragmentSleepTrackerBinding
import com.example.sleeptracker.recyclerview.SleepNightAdapter

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_tracker, container, false)
        // Create Piping for sleepDatabase & UI Controller
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val sleepTrackerViewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val sleepTrackerViewModel = ViewModelProvider(this, sleepTrackerViewModelFactory).get(SleepTrackerViewModel::class.java)
        binding.sleepTrackerViewModel = sleepTrackerViewModel
        binding.lifecycleOwner = this

        val adapter = SleepNightAdapter()
        binding.sleepList.adapter = adapter
        sleepTrackerViewModel.sleepNightsList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.data = it
            }
        }

        sleepTrackerViewModel.eventStopSleepTracking.observe(viewLifecycleOwner) { hasStopClicked ->
            if (hasStopClicked) {
                handleStopClick()
                sleepTrackerViewModel.onStopTrackingComplete()
            }
        }

        return binding.root
    }

    private fun handleStopClick() {
        findNavController().navigate(R.id.action_sleepTrackerFragment_to_sleepQualityFragment2)
    }
}