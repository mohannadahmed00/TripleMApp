package com.giraffe.triplemapplication.features.fav.swipe


import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(context:Context) : ItemTouchHelper.SimpleCallback(0 ,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    val deleteColor = ContextCompat.getColor(context, R.color.deleteColor)

    val deleteIcon = R.drawable.delete_ic

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {

        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(deleteIcon)
            .addCornerRadius(1 ,5)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }
}
