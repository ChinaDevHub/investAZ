package com.example.investaz.presentation.main.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.investaz.R
import com.example.investaz.databinding.ItemPriceBinding
import com.example.investaz.presentation.common.flash
import com.example.investaz.presentation.main.model.PriceUiModel

class PriceViewHolder(private val binding: ItemPriceBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PriceUiModel) = with(binding) {
        val context = root.context
        val trendColor = ContextCompat.getColor(context, item.trendColor)
        val trendTint = ColorStateList.valueOf(trendColor)

        avatar.text = item.initials
        avatar.setTextColor(trendColor)
        avatar.backgroundTintList = trendTint.withAlpha(AVATAR_TINT_ALPHA)
        symbol.text = item.symbol
        meta.text = context.getString(R.string.price_meta, item.spread, item.updatedAt)

        bid.text = item.bid
        bid.setTextColor(trendColor)
        ask.text = context.getString(R.string.price_ask, item.ask)
        trendIcon.setImageResource(item.trendIcon)
        ImageViewCompat.setImageTintList(trendIcon, trendTint)

        low.text = context.getString(R.string.price_low, item.low)
        high.text = context.getString(R.string.price_high, item.high)
        range.setIndicatorColor(trendColor)
        range.setProgressCompat(item.rangeProgress, false)
    }

    fun highlightPriceTick() = binding.bid.flash()

    companion object {
        private const val AVATAR_TINT_ALPHA = 36

        fun create(parent: ViewGroup) = PriceViewHolder(
            ItemPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}
