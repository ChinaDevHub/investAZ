package com.example.investaz.presentation.common

import android.view.View

private const val FLASH_START_ALPHA = 0.3f
private const val FLASH_DURATION_MS = 400L

/** Briefly dims and restores the view to draw attention to a changed value. */
fun View.flash() {
    animate().cancel()
    alpha = FLASH_START_ALPHA
    animate().alpha(1f).setDuration(FLASH_DURATION_MS).start()
}
