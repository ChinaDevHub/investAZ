package com.example.investaz.presentation.main.components

import androidx.core.content.ContextCompat
import com.example.investaz.R
import com.example.investaz.databinding.LayoutMarketSummaryBinding
import com.example.investaz.databinding.LayoutStatBinding
import com.example.investaz.presentation.common.UiComponent
import com.example.investaz.presentation.main.MainUiState

class MarketSummaryComponent(private val binding: LayoutMarketSummaryBinding) : UiComponent<MainUiState> {

    init {
        binding.total.setup(R.string.summary_instruments, R.color.on_brand)
        binding.gainers.setup(R.string.summary_gainers, R.color.signal_positive)
        binding.losers.setup(R.string.summary_losers, R.color.signal_negative)
    }

    override fun render(state: MainUiState) {
        val summary = state.summary
        binding.total.value.text = summary.total.toString()
        binding.gainers.value.text = summary.gainers.toString()
        binding.losers.value.text = summary.losers.toString()
        binding.sentimentValue.text = if (state.hasCachedPrices) {
            binding.root.context.getString(R.string.summary_sentiment_value, summary.gainersPercent)
        } else {
            binding.root.context.getString(R.string.summary_sentiment_empty)
        }
        binding.sentiment.setProgressCompat(summary.gainersPercent, true)
    }

    private fun LayoutStatBinding.setup(label: Int, valueColor: Int) {
        this.label.setText(label)
        value.setTextColor(ContextCompat.getColor(root.context, valueColor))
    }
}
