package com.example.investaz.domain.model

data class PriceItem(
    val symbol: String,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis()
)