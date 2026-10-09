package com.example.investaz.presentation.main.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

fun RecyclerView.setupPriceList(priceAdapter: PriceAdapter) {
    layoutManager = LinearLayoutManager(context)
    adapter = priceAdapter
    setHasFixedSize(true)
    // Rows update every tick; cross-fade change animations would make the list flicker.
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}
