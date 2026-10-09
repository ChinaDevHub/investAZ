package com.example.investaz.presentation.main.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.investaz.R
import com.example.investaz.domain.model.ConnectionStatus

data class StatusBadge(@param:StringRes val label: Int, @param:ColorRes val dotColor: Int)

fun ConnectionStatus.toBadge(): StatusBadge = when (this) {
    ConnectionStatus.CONNECTED -> StatusBadge(R.string.status_connected, R.color.signal_positive)
    ConnectionStatus.CONNECTING -> StatusBadge(R.string.status_connecting, R.color.signal_warning)
    ConnectionStatus.DISCONNECTED -> StatusBadge(R.string.status_disconnected, R.color.signal_negative)
}
