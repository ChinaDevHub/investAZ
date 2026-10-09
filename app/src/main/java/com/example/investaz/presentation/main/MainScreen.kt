package com.example.investaz.presentation.main

import com.example.investaz.databinding.FragmentMainBinding
import com.example.investaz.domain.model.PriceFilter
import com.example.investaz.presentation.common.UiComponent
import com.example.investaz.presentation.main.components.BodyComponent
import com.example.investaz.presentation.main.components.EmptyOrErrorComponent
import com.example.investaz.presentation.main.components.FilterComponent
import com.example.investaz.presentation.main.components.HeaderComponent
import com.example.investaz.presentation.main.components.MarketSummaryComponent

class MainScreen(
    binding: FragmentMainBinding,
    onFilterSelected: (PriceFilter) -> Unit,
) : UiComponent<MainUiState> {

    private val components: List<UiComponent<MainUiState>> = listOf(
        HeaderComponent(binding.header),
        MarketSummaryComponent(binding.header.summary),
        FilterComponent(binding.filters, onFilterSelected),
        BodyComponent(binding.body),
        EmptyOrErrorComponent(binding.emptyOrError),
    )

    override fun render(state: MainUiState) = components.forEach { it.render(state) }
}
