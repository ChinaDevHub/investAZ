package com.example.investaz.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.investaz.presentation.main.model.PriceUiModel

object PriceDiffCallback : DiffUtil.ItemCallback<PriceUiModel>() {

    /** Payload telling the view holder that a quote ticked, so it can highlight the new price. */
    const val PRICE_TICK = "price_tick"

    override fun areItemsTheSame(oldItem: PriceUiModel, newItem: PriceUiModel) = oldItem.symbol == newItem.symbol

    override fun areContentsTheSame(oldItem: PriceUiModel, newItem: PriceUiModel) = oldItem == newItem

    override fun getChangePayload(oldItem: PriceUiModel, newItem: PriceUiModel): Any? =
        PRICE_TICK.takeIf { oldItem.bid != newItem.bid || oldItem.ask != newItem.ask }
}
