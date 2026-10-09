package com.example.investaz.presentation.main.components

import androidx.core.view.isVisible
import com.example.investaz.databinding.LayoutEmptyOrErrorBinding
import com.example.investaz.presentation.common.UiComponent
import com.example.investaz.presentation.main.MainUiState

class EmptyOrErrorComponent(private val binding: LayoutEmptyOrErrorBinding) : UiComponent<MainUiState> {

    override fun render(state: MainUiState) {
        val placeholder = state.placeholder
        binding.root.isVisible = placeholder != null
        if (placeholder == null) return

        binding.progress.isVisible = placeholder.icon == null
        binding.icon.isVisible = placeholder.icon != null
        placeholder.icon?.let(binding.icon::setImageResource)
        binding.title.setText(placeholder.title)
        binding.message.setText(placeholder.message)
    }
}
