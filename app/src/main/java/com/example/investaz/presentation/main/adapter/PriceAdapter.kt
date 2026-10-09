package com.example.investaz.presentation.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.investaz.presentation.main.model.PriceUiModel

class PriceAdapter : ListAdapter<PriceUiModel, PriceViewHolder>(PriceDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder =
        PriceViewHolder.create(parent)

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.bind(getItem(position))
        if (PriceDiffCallback.PRICE_TICK in payloads) holder.highlightPriceTick()
    }
}
