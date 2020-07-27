package com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

/**
 * Scroll event listener which notifies when the end of the recycler view is reached.
 * Supports only [LinearLayoutManager]
 */
class VerticalScrollEndListener(
    private val layoutManager: LinearLayoutManager,
    private val onEndReached: () -> Unit
) : OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val lastItemPosition = layoutManager.itemCount - 1
        if (layoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition) {
            onEndReached()
        }
    }
}