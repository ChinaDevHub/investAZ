package com.example.investaz.presentation.common

fun interface UiComponent<S> {
    fun render(state: S)
}
