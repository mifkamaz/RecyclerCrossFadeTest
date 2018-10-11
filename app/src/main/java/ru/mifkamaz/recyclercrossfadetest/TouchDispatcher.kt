package ru.mifkamaz.recyclercrossfadetest

import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent

class TouchDispatcher(
    private val layoutManager: LinearLayoutManager
) {

    companion object {

        private const val MOTION_PRECISION = 10F

    }

    private var actionDownX: Float = 0F

    fun onTouch(motion: MotionEvent) {
        if (motion.action == MotionEvent.ACTION_DOWN) {
            actionDownX = motion.x
            return
        }

        if (motion.action != MotionEvent.ACTION_UP) {
            return
        }

        if (Math.abs(actionDownX - motion.x) > MOTION_PRECISION) {
            return
        }

        val layoutManager = layoutManager as? LinearLayoutManager ?: return

        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        val itemsCount = lastVisible - firstVisible + 1

        (0 until itemsCount)
            .map {
                layoutManager.getChildAt(it)
            }
            .filterNotNull()
            .filter {
                it.left < motion.x && motion.x < it.right
            }
            .take(1)
            .forEach {
                it.performClick()
            }
    }

}