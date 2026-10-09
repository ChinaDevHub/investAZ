package com.example.investaz.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.investaz.R
import com.example.investaz.databinding.FragmentMainBinding
import com.example.investaz.presentation.common.SyncLifecycleObserver
import com.example.investaz.presentation.common.collectOnStarted

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory.Default }

    // Binding and components live only inside the view lifecycle, so nothing outlives the view.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val screen = MainScreen(FragmentMainBinding.bind(view), viewModel::onFilterSelected)
        viewLifecycleOwner.lifecycle.addObserver(SyncLifecycleObserver(viewModel))
        viewLifecycleOwner.collectOnStarted(viewModel.uiState, screen::render)
    }
}
