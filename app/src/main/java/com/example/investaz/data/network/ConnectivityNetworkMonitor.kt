package com.example.investaz.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * A dead socket is otherwise only noticed after the Engine.IO ping timeout (~85s on this server);
 * following the default network lets the app drop and restore the connection immediately.
 */
class ConnectivityNetworkMonitor(context: Context) : NetworkMonitor {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    override val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            private var current: Network? = null

            override fun onAvailable(network: Network) {
                current = network
                send(online = true)
            }

            // On a Wi-Fi -> mobile handover the old network can be lost *after* the new one is
            // available; only losing the current default network means we are offline.
            override fun onLost(network: Network) {
                if (network != current) return
                current = null
                send(online = false)
            }

            private fun send(online: Boolean) {
                Log.i(TAG, "Default network online=$online")
                trySend(online)
            }
        }
        trySend(connectivityManager.activeNetwork != null)
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.conflate().distinctUntilChanged()

    private companion object {
        const val TAG = "ConnectivityMonitor"
    }
}
