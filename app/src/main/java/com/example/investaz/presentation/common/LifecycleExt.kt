package com.example.investaz.presentation.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/** Collects on the main thread only while the owner is at least STARTED. */
fun <T> LifecycleOwner.collectOnStarted(flow: Flow<T>, action: (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collect(action) }
    }
}
