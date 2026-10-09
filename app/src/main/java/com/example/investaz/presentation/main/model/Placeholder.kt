package com.example.investaz.presentation.main.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.investaz.R

/** [icon] is null while loading, where a progress indicator is shown instead. */
enum class Placeholder(
    @param:StringRes val title: Int,
    @param:StringRes val message: Int,
    @param:DrawableRes val icon: Int?,
) {
    LOADING(R.string.placeholder_loading_title, R.string.placeholder_loading_message, null),
    OFFLINE(R.string.placeholder_offline_title, R.string.placeholder_offline_message, R.drawable.ic_cloud_off),
    NO_MATCHES(R.string.placeholder_no_matches_title, R.string.placeholder_no_matches_message, R.drawable.ic_filter),
}
