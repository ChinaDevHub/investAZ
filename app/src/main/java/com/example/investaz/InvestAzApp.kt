package com.example.investaz

import android.app.Application
import com.example.investaz.di.AppContainer
import com.example.investaz.di.DefaultAppContainer

class InvestAzApp : Application() {
    val container: AppContainer by lazy { DefaultAppContainer(this) }
}
