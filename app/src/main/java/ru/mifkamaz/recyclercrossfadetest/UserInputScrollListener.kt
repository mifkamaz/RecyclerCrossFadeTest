package ru.mifkamaz.recyclercrossfadetest

import android.support.v7.widget.RecyclerView

class UserInputScrollListener(
    private val onStarted: () -> Unit,
    private val onEnded: () -> Unit
) : RecyclerView.OnScrollListener() {

    private val startPattern = arrayOf(RecyclerView.SCROLL_STATE_IDLE, RecyclerView.SCROLL_STATE_DRAGGING)

    private val endDraggingPattern = arrayOf(
        RecyclerView.SCROLL_STATE_DRAGGING,
        RecyclerView.SCROLL_STATE_SETTLING,
        RecyclerView.SCROLL_STATE_IDLE
    )

    private val endForcePattern = arrayOf(
        RecyclerView.SCROLL_STATE_DRAGGING,
        RecyclerView.SCROLL_STATE_IDLE
    )

    private val scrollStateHistory = mutableListOf(RecyclerView.SCROLL_STATE_IDLE)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        scrollStateHistory.add(newState)

        if (scrollStateHistory.hasEndsWith(startPattern)) {
            onStarted()
        }

        if (scrollStateHistory.hasEndsWith(endDraggingPattern) || scrollStateHistory.hasEndsWith(endForcePattern)) {
            onEnded()
        }
    }

    private fun <T> List<T>.hasEndsWith(end: Array<T>): Boolean {
        val offset = size - end.size

        if (offset < 0) {
            return false
        }

        end.forEachIndexed { index, value ->
            if (get(offset + index) != value) {
                return false
            }
        }

        return true
    }

}