package com.example.investaz.data.remote

import android.util.Log
import com.example.investaz.domain.model.ConnectionStatus
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow

class SocketManager(private val config: SocketConfig) : PriceSocketClient {

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    override val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val socket: Socket by lazy { createSocket() }

    override fun connect() {
        if (socket.connected()) return
        _connectionStatus.value = ConnectionStatus.CONNECTING
        socket.connect()
    }

    override fun disconnect() {
        socket.disconnect()
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
    }

    // Every payload is a full snapshot, so only the latest one matters to a slow collector.
    override fun messages(): Flow<String> = callbackFlow {
        val listener = Emitter.Listener { args ->
            val payload = args.firstOrNull()
            if (payload == null) Log.w(TAG, "Ignoring empty '${config.eventName}' event")
            else trySend(payload.toString())
        }
        socket.on(config.eventName, listener)
        awaitClose { socket.off(config.eventName, listener) }
    }.buffer(Channel.CONFLATED)

    private fun createSocket(): Socket = IO.socket(config.baseUrl, config.toOptions()).apply {
        on(Socket.EVENT_CONNECT) { _connectionStatus.value = ConnectionStatus.CONNECTED }
        on(Socket.EVENT_DISCONNECT) { args ->
            Log.i(TAG, "Disconnected: ${args.firstOrNull()}")
            _connectionStatus.value = ConnectionStatus.DISCONNECTED
        }
        on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.w(TAG, "Connection error: ${args.firstOrNull()}")
            _connectionStatus.value = ConnectionStatus.DISCONNECTED
        }
        io().on(Manager.EVENT_RECONNECT_ATTEMPT) {
            _connectionStatus.value = ConnectionStatus.CONNECTING
        }
    }

    private companion object {
        const val TAG = "SocketManager"
    }
}
