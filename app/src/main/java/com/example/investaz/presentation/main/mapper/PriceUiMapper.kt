package com.example.investaz.presentation.main.mapper

import com.example.investaz.R
import com.example.investaz.domain.model.PriceDirection
import com.example.investaz.domain.model.PriceItem
import com.example.investaz.presentation.main.model.PriceUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/** Not thread-safe: meant to be used sequentially from a single flow. */
class PriceUiMapper(locale: Locale = Locale.getDefault()) {

    private val timeFormat = SimpleDateFormat("HH:mm:ss", locale)

    fun map(item: PriceItem): PriceUiModel = PriceUiModel(
        symbol = item.symbol,
        initials = item.symbol.take(INITIALS_LENGTH),
        bid = item.bid.toPlainString(),
        ask = item.ask.toPlainString(),
        low = item.low.toPlainString(),
        high = item.high.toPlainString(),
        spread = item.spread,
        updatedAt = timeFormat.format(Date(item.updatedAt)),
        rangeProgress = (item.rangePosition * 100).roundToInt(),
        trendColor = item.direction.colorRes(),
        trendIcon = item.direction.iconRes(),
    )

    private fun PriceDirection.colorRes(): Int = when (this) {
        PriceDirection.UP -> R.color.price_up
        PriceDirection.DOWN -> R.color.price_down
        PriceDirection.UNCHANGED -> R.color.price_neutral
    }

    private fun PriceDirection.iconRes(): Int = when (this) {
        PriceDirection.UP -> R.drawable.ic_trend_up
        PriceDirection.DOWN -> R.drawable.ic_trend_down
        PriceDirection.UNCHANGED -> R.drawable.ic_trend_flat
    }

    private companion object {
        const val INITIALS_LENGTH = 2
    }
}
