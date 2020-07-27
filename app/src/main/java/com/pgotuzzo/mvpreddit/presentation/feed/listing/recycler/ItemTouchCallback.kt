package com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pgotuzzo.mvpreddit.R

class ItemTouchCallback(
    context: Context,
    private val onDelete: (adapterPosition: Int) -> Unit
) : SimpleCallback(0, LEFT) {

    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val background: ColorDrawable =
        ColorDrawable(ContextCompat.getColor(context, R.color.secondaryDarkColor))

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        onDelete(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        // Swiping to the left
        if (dX < 0) {
            val itemView = viewHolder.itemView
            // Background
            background.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
            background.draw(c)
            // Icon
            deleteIcon?.also {
                val iconMargin: Int = (itemView.height - it.intrinsicHeight) / 2
                val iconTop: Int = itemView.top + (itemView.height - it.intrinsicHeight) / 2
                val iconBottom: Int = iconTop + it.intrinsicHeight
                val iconLeft: Int = itemView.right - iconMargin - it.intrinsicWidth
                val iconRight: Int = itemView.right - iconMargin
                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                it.draw(c)
            }
        }
    }
}