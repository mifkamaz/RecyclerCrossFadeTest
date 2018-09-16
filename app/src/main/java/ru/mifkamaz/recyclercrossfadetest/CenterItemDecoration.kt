package ru.mifkamaz.recyclercrossfadetest

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup

/**
 * This decoration allows first and last item of horizontally oriented recycler view to be centered
 * by adding specific margin to start for first elements and to the end for last element
 * @author novikov
 *         Date: 06.02.2018
 */
class CenterItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        val itemCount = state.itemCount
        if (itemPosition > 0 && itemPosition < itemCount - 1) {
            return
        }
        // careful with unspecified
        val width = if (view.layoutParams.width < 0) {
            view.measure(
                makeMeasureSpec(0, UNSPECIFIED),
                makeMeasureSpec(0, UNSPECIFIED)
            )
            view.measuredWidth
        } else {
            view.layoutParams.width
        }

        val parentWidth = if (state.isMeasuring) {
            parent.layoutManager?.width ?: 0 - (parent.layoutParams as ViewGroup.MarginLayoutParams).run {
                marginStart + marginEnd
            }
        } else {
            parent.width
        }

        val offset = (view.layoutParams as ViewGroup.MarginLayoutParams).run {
            when (itemPosition) {
                0 -> leftMargin
                else -> rightMargin
            }
        }

        val mPaddingEdgesPx = parentWidth / 2 - width / 2 - offset

        val orientation = getOrientation(parent)

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            /** first position  */
            if (itemPosition == 0) {
                left += mPaddingEdgesPx
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                right += mPaddingEdgesPx
            }
            /** last position  */
        } else {
            /** first position  */
            if (itemPosition == 0) {
                top += mPaddingEdgesPx
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                bottom += mPaddingEdgesPx
            }
            /** last position  */
        }

        if (!isReverseLayout(parent)) {
            outRect.set(left, top, right, bottom)
        } else {
            outRect.set(right, bottom, left, top)
        }
    }

    private fun isReverseLayout(parent: RecyclerView): Boolean {
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            return layoutManager.reverseLayout
        } else {
            throw IllegalStateException("PaddingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            return layoutManager.orientation
        } else {
            throw IllegalStateException("PaddingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }
}