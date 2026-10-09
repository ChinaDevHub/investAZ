package com.example.investaz.presentation.main.components

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.example.investaz.R
import com.example.investaz.databinding.LayoutHeaderBinding
import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.presentation.common.UiComponent
import com.example.investaz.presentation.common.applySystemBarsPadding
import com.example.investaz.presentation.main.MainUiState
import com.example.investaz.presentation.main.model.toBadge

class HeaderComponent(private val binding: LayoutHeaderBinding) : UiComponent<MainUiState> {

    init {
        binding.root.applySystemBarsPadding(top = true)
    }

    override fun render(state: MainUiState) {
        val badge = state.connectionStatus.toBadge()
        binding.statusChip.setText(badge.label)
        binding.statusChip.chipIconTint =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, badge.dotColor))
        binding.subtitle.setText(state.subtitleRes())
    }

    private fun MainUiState.subtitleRes(): Int = when {
        connectionStatus == ConnectionStatus.CONNECTED -> R.string.header_subtitle_live
        hasCachedPrices -> R.string.header_subtitle_cached
        else -> R.string.header_subtitle_waiting
    }
}
