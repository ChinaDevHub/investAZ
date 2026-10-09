package com.example.investaz.presentation.main.components

import androidx.core.view.isVisible
import com.example.investaz.databinding.LayoutBodyBinding
import com.example.investaz.presentation.common.UiComponent
import com.example.investaz.presentation.common.applySystemBarsPadding
import com.example.investaz.presentation.main.MainUiState
import com.example.investaz.presentation.main.adapter.PriceAdapter
import com.example.investaz.presentation.main.adapter.setupPriceList

class BodyComponent(private val binding: LayoutBodyBinding) : UiComponent<MainUiState> {

    private val adapter = PriceAdapter()

    init {
        binding.priceList.setupPriceList(adapter)
        binding.priceList.applySystemBarsPadding(bottom = true)
    }

    override fun render(state: MainUiState) {
        binding.root.isVisible = state.prices.isNotEmpty()
        // Rows inserted above the first visible one would otherwise push the top of the list off-screen.
        val wasAtTop = !binding.priceList.canScrollVertically(-1)
        adapter.submitList(state.prices) { if (wasAtTop) binding.priceList.scrollToPosition(0) }
    }
}
