package ru.mifkamaz.recyclercrossfadetest

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class CenterHelper(private val controlScrollState: ScrollState) : RecyclerView.OnScrollListener() {

    var listener: ((Int) -> Unit)? = null

    var lastScrollPosition: Int? = null

    fun attachToRecyclerView(recyclerView: RecyclerView, listener: (Int) -> Unit = {}) {
        this.listener = listener
        recyclerView.addOnScrollListener(this)
    }

    fun dettachFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        findAndPost(recyclerView, controlScrollState == ScrollState.SCROLL)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        findAndPost(
            recyclerView,
            controlScrollState == ScrollState.IDLE && newState == RecyclerView.SCROLL_STATE_IDLE
        )
    }

    private fun findAndPost(recyclerView: RecyclerView?, post: Boolean) {
        val middleItemIndex = findCenterViewPosition(recyclerView)

        middleItemIndex ?: return

        if (lastScrollPosition != middleItemIndex && post) {
            lastScrollPosition = middleItemIndex
            listener?.invoke(middleItemIndex)
        }
    }

    companion object {

        fun findCenterViewPosition(recyclerView: RecyclerView?): Int? {
            recyclerView ?: return null

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val itemsCount = lastVisible - firstVisible + 1

            val screenCenter =
                recyclerView.context.getResources().getDisplayMetrics().widthPixels / 2

            var minCenterOffset = Integer.MAX_VALUE

            var middleItemIndex = 0

            for (index in 0 until itemsCount) {

                val listItem = layoutManager.getChildAt(index) ?: return null

                val leftOffset = listItem.left
                val rightOffset = listItem.right
                val centerOffset =
                    Math.abs(leftOffset - screenCenter) + Math.abs(rightOffset - screenCenter)
                listItem.isSelected = false

                if (minCenterOffset > centerOffset) {
                    minCenterOffset = centerOffset
                    middleItemIndex = index + firstVisible
                }
            }

            layoutManager.getChildAt(middleItemIndex - firstVisible)?.isSelected = true

            return middleItemIndex
        }

        fun moveViewToCenter(recycler: RecyclerView, adapterPosition: Int) {
            val manager = recycler.layoutManager as LinearLayoutManager

            val firstVisiblePosition = manager.findFirstVisibleItemPosition()
            val lastVisiblePosition = manager.findLastVisibleItemPosition()

            if (firstVisiblePosition > adapterPosition || adapterPosition > lastVisiblePosition) {
                manager.scrollToPosition(adapterPosition)
            }

            recycler.post {
                val manager = recycler.layoutManager as LinearLayoutManager

                val firstVisiblePosition = manager.findFirstVisibleItemPosition()
                val centerPosition = CenterHelper.findCenterViewPosition(recycler) ?: return@post

                val clickedView =
                    manager.getChildAt(adapterPosition - firstVisiblePosition) ?: return@post
                val centerView =
                    manager.getChildAt(centerPosition - firstVisiblePosition) ?: return@post

                recycler.smoothScrollBy(
                    clickedView.left - centerView.left,
                    clickedView.top - centerView.top
                )
            }
        }

    }

    enum class ScrollState {
        SCROLL,
        IDLE
    }
}