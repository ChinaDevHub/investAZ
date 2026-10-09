package com.example.investaz.data.mapper

import com.example.investaz.data.local.entity.PriceEntity
import com.example.investaz.data.remote.dto.PriceDto
import com.example.investaz.domain.model.PriceDirection
import com.example.investaz.domain.model.PriceItem
import java.math.BigDecimal

fun PriceDto.toEntity(receivedAt: Long): PriceEntity = PriceEntity(
    symbol = symbol,
    direction = direction,
    bid = bid,
    ask = ask,
    low = low,
    high = high,
    spread = spread,
    updatedAt = IsoTimestampParser.toEpochMillis(time) ?: receivedAt,
)

fun PriceEntity.toDomain(): PriceItem = PriceItem(
    symbol = symbol,
    bid = bid.toDecimal(),
    ask = ask.toDecimal(),
    low = low.toDecimal(),
    high = high.toDecimal(),
    spread = spread,
    direction = direction.toDirection(),
    updatedAt = updatedAt,
)

private fun String.toDecimal(): BigDecimal = toBigDecimalOrNull() ?: BigDecimal.ZERO

private fun String.toDirection(): PriceDirection = when (lowercase()) {
    "up" -> PriceDirection.UP
    "down" -> PriceDirection.DOWN
    else -> PriceDirection.UNCHANGED
}
