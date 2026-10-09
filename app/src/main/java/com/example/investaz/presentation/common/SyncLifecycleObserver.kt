package com.example.investaz.presentation.common

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/** Keeps the live connection open only while the screen is visible. */
class SyncLifecycleObserver(private val controller: SyncController) : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) = controller.startSync()
    override fun onStop(owner: LifecycleOwner) = controller.stopSync()
}
