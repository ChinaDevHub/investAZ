package com.example.investaz.presentation.main.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class PriceUiModel(
    val symbol: String,
    val initials: String,
    val bid: String,
    val ask: String,
    val low: String,
    val high: String,
    val spread: Int,
    val updatedAt: String,
    /** Position of the bid inside the day's range, 0..100. */
    val rangeProgress: Int,
    @param:ColorRes val trendColor: Int,
    @param:DrawableRes val trendIcon: Int,
)
