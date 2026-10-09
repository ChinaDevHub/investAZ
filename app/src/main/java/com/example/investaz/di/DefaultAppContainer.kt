package com.example.investaz.di

import android.content.Context
import com.example.investaz.data.local.PriceDatabase
import com.example.investaz.data.network.ConnectivityNetworkMonitor
import com.example.investaz.data.remote.SocketConfig
import com.example.investaz.data.remote.SocketManager
import com.example.investaz.data.remote.parser.PriceMessageParser
import com.example.investaz.data.repository.PriceRepositoryImpl
import com.example.investaz.domain.repository.PriceRepository
import kotlinx.coroutines.Dispatchers

/** Composition root: the only place that knows the concrete implementations. */
class DefaultAppContainer(context: Context) : AppContainer {

    private val appContext = context.applicationContext

    override val priceRepository: PriceRepository by lazy {
        PriceRepositoryImpl(
            socketClient = SocketManager(SocketConfig.Default),
            networkMonitor = ConnectivityNetworkMonitor(appContext),
            priceDao = PriceDatabase.create(appContext).priceDao(),
            parser = PriceMessageParser(),
            ioDispatcher = Dispatchers.IO,
        )
    }
}
