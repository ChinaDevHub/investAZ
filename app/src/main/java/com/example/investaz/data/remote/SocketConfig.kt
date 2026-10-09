package com.example.investaz.data.remote

import io.socket.client.IO

data class SocketConfig(
    val baseUrl: String,
    val path: String,
    val eventName: String,
    val reconnectionDelayMs: Long = 1_000,
    val reconnectionDelayMaxMs: Long = 5_000,
    val timeoutMs: Long = 10_000,
) {
    // Default transports (long-polling, then upgrade to WebSocket) also work on networks that block WebSockets.
    fun toOptions(): IO.Options = IO.Options().also {
        it.path = path
        it.reconnection = true
        it.reconnectionAttempts = Int.MAX_VALUE
        it.reconnectionDelay = reconnectionDelayMs
        it.reconnectionDelayMax = reconnectionDelayMaxMs
        it.timeout = timeoutMs
    }

    companion object {
        val Default = SocketConfig(
            baseUrl = "https://q.investaz.az",
            path = "/live",
            eventName = "message",
        )
    }
}
