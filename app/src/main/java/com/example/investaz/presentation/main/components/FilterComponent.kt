package com.example.investaz.presentation.main.components

import com.example.investaz.R
import com.example.investaz.databinding.LayoutFiltersBinding
import com.example.investaz.domain.model.PriceFilter
import com.example.investaz.presentation.common.UiComponent
import com.example.investaz.presentation.main.MainUiState

class FilterComponent(
    private val binding: LayoutFiltersBinding,
    onFilterSelected: (PriceFilter) -> Unit,
) : UiComponent<MainUiState> {

    private val chips = mapOf(
        PriceFilter.ALL to binding.chipAll,
        PriceFilter.GAINERS to binding.chipGainers,
        PriceFilter.LOSERS to binding.chipLosers,
    )

    init {
        binding.root.setOnCheckedStateChangeListener { _, checkedIds ->
            chips.entries.firstOrNull { it.value.id in checkedIds }?.let { onFilterSelected(it.key) }
        }
    }

    override fun render(state: MainUiState) {
        chips.forEach { (filter, chip) ->
            chip.text = chip.context.getString(filter.labelRes(), state.summary.countFor(filter))
        }
        val selected = chips.getValue(state.filter)
        if (!selected.isChecked) binding.root.check(selected.id)
    }

    private fun PriceFilter.labelRes(): Int = when (this) {
        PriceFilter.ALL -> R.string.filter_all
        PriceFilter.GAINERS -> R.string.filter_gainers
        PriceFilter.LOSERS -> R.string.filter_losers
    }
}
