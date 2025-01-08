package com.example.sleeptracker.sleepquality

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
import com.example.sleeptracker.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quality, container, false)

        val application = requireNotNull(this.activity).application
        val args = SleepQualityFragmentArgs.fromBundle(requireArguments())
        val database = SleepDatabase.getInstance(application).sleepDatabaseDao
        val sleepQualityViewModelFactory = SleepQualityViewModelFactory(args.sleepNightKey, database)
        val sleepQualityViewModel = ViewModelProvider(this, sleepQualityViewModelFactory).get(SleepQualityViewModel::class.java)

        binding.sleepQualityViewModel = sleepQualityViewModel
        binding.lifecycleOwner = this

        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) { canNavigate ->
            if (canNavigate == true) {
                findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment2())
                sleepQualityViewModel.doneNavigating()
            }
        }


        return binding.root
    }
}