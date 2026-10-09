package com.example.investaz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Prices are stored as text to keep the exact precision sent by the server (e.g. "0.99240").
@Entity(tableName = "prices")
data class PriceEntity(
    @PrimaryKey val symbol: String,
    val direction: String,
    val bid: String,
    val ask: String,
    val low: String,
    val high: String,
    val spread: Int,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)
