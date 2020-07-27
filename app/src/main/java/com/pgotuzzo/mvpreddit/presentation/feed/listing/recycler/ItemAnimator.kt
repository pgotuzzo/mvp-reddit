package com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.addListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Provides recycler view item animations for item's removal events.
 */
class ItemAnimator : DefaultItemAnimator() {

    override fun animateRemove(holder: ViewHolder?): Boolean =
        holder?.let {
            val initPos = it.itemView.x
            val finalPos = it.itemView.x - it.itemView.width.toFloat()
            ObjectAnimator.ofFloat(it.itemView, View.X, initPos, finalPos).apply {
                duration = 500
                addListener(
                    onStart = { dispatchRemoveStarting(holder) },
                    onEnd = { _ ->
                        dispatchRemoveFinished(holder)
                        // Reset value after animation ends
                        it.itemView.x = 0f
                    })
                start()
            }
            true
        } ?: false
}