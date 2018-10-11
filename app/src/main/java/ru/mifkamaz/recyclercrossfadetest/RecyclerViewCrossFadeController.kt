package ru.mifkamaz.recyclercrossfadetest

import android.animation.ObjectAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View

class RecyclerViewCrossFadeController(
    overlayView: View,
    private val primaryRecycler: RecyclerView,
    private val secondaryRecycler: RecyclerView,
    beginCenterPosition: Int
) : View.OnTouchListener {

    companion object {
        private const val ANIMATION_DELAY = 0L
        private const val ANIMATION_DURATION = 0L
    }

    private val touchDispatcher = TouchDispatcher(primaryRecycler.layoutManager as LinearLayoutManager)

    private val hidePrimaryAnimation = createAnimation(1F, 0F, ANIMATION_DURATION, 0)

    private var showPrimaryAnimation = createAnimation(0F, 1F, ANIMATION_DURATION, ANIMATION_DELAY)

    init {
        validate()

        primaryRecycler.bringToFront()
        overlayView.bringToFront()

        primaryRecycler.addItemDecoration(CenterItemDecoration())
        secondaryRecycler.addItemDecoration(CenterItemDecoration())

        overlayView.setOnTouchListener(this)

        secondaryRecycler.addOnScrollListener(UserInputScrollListener(::hidePrimary, ::showPrimary))

        CenterHelper.moveViewToCenter(secondaryRecycler, beginCenterPosition, false)
        CenterHelper.moveViewToCenter(primaryRecycler, beginCenterPosition, false)

        CustomLinearSnapHelper().attachToRecyclerView(primaryRecycler)

        CenterHelper(CenterHelper.ScrollState.SCROLL, false).attachToRecyclerView(secondaryRecycler) {
            CenterHelper.moveViewToCenter(primaryRecycler, it, false)
        }
    }

    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
        motionEvent ?: return false
        touchDispatcher.onTouch(motionEvent)
        return secondaryRecycler.dispatchTouchEvent(motionEvent)
    }

    private fun hidePrimary() {
        showPrimaryAnimation.cancel()
        hidePrimaryAnimation.start()
    }

    private fun showPrimary() {
        hidePrimaryAnimation.cancel()
        showPrimaryAnimation.start()
    }

    private fun validate() {
        if (primaryRecycler.layoutManager == null) {
            throw IllegalArgumentException()
        }

        if (secondaryRecycler.layoutManager == null) {
            throw IllegalArgumentException()
        }

        val primaryAdapter = primaryRecycler.adapter
        val secondaryAdapter = secondaryRecycler.adapter

        if (primaryAdapter == null) {
            throw IllegalArgumentException()
        }

        if (secondaryAdapter == null) {
            throw IllegalArgumentException()
        }

        if (primaryAdapter.itemCount != secondaryAdapter.itemCount) {
            throw IllegalArgumentException()
        }
    }

    private fun createAnimation(startVal: Float, endVal: Float, duration: Long = 0, delay: Long = 0) =
        ObjectAnimator.ofFloat(primaryRecycler, "alpha", startVal, endVal).apply {
            this.duration = duration
            this.startDelay = delay
        }

}