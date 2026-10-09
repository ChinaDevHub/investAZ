package com.example.investaz.data.remote.dto

/** One quote exactly as sent by the server, positional keys "0".."7". */
data class PriceDto(
    val direction: String,
    val symbol: String,
    val bid: String,
    val ask: String,
    val low: String,
    val high: String,
    val spread: Int,
    val time: String,
)
