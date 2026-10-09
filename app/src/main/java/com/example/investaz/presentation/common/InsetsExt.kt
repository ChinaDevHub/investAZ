package com.example.investaz.presentation.common

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/** Adds the system bar insets on the requested sides on top of the view's own padding. */
fun View.applySystemBarsPadding(top: Boolean = false, bottom: Boolean = false) {
    val initialTop = paddingTop
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            top = initialTop + if (top) bars.top else 0,
            bottom = initialBottom + if (bottom) bars.bottom else 0,
        )
        insets
    }
}
