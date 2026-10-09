package com.example.investaz.di

import com.example.investaz.domain.repository.PriceRepository

interface AppContainer {
    val priceRepository: PriceRepository
}
