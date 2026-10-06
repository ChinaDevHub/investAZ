package com.example.investaz.data.remote

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow

class SocketManager {

    private var socket: Socket? = null

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Disconnected)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    sealed class ConnectionStatus {
        object Connected : ConnectionStatus()
        object Connecting : ConnectionStatus()
        object Disconnected : ConnectionStatus()
        data class Error(val message: String) : ConnectionStatus()
    }

    fun connect() {
        if (socket?.connected() == true) return

        try {
            _connectionStatus.value = ConnectionStatus.Connecting

            val options = IO.Options().apply {
                path = "/live"
                reconnection = true
                reconnectionAttempts = Int.MAX_VALUE
                reconnectionDelay = 2000
                timeout = 10000
            }

            socket = IO.socket("https://q.investaz.az", options)

            val onConnect = Emitter.Listener {
                _connectionStatus.value = ConnectionStatus.Connected
            }

            val onDisconnect = Emitter.Listener {
                _connectionStatus.value = ConnectionStatus.Disconnected
            }

            val onError = Emitter.Listener { args ->
                val errorMsg = args.getOrNull(0)?.toString() ?: "Connection error"
                _connectionStatus.value = ConnectionStatus.Error(errorMsg)
            }

            socket?.on(Socket.EVENT_CONNECT, onConnect)
            socket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket?.on(Socket.EVENT_CONNECT_ERROR, onError)

            socket?.connect()
        } catch (e: Exception) {
            _connectionStatus.value = ConnectionStatus.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    fun observePrices(): Flow<String> = callbackFlow {
        val listener = Emitter.Listener { args ->
            if (args.isNotEmpty()) {
                val data = args[0].toString()
                trySend(data)
            }
        }

        socket?.on("message", listener)

        awaitClose {
            socket?.off("message", listener)
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
        socket = null
        _connectionStatus.value = ConnectionStatus.Disconnected
    }
}