package ru.mifkamaz.recyclercrossfadetest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private val centerHelperLarge = CenterHelper(CenterHelper.ScrollState.SCROLL)
    private val centerHelperSmall = CenterHelper(CenterHelper.ScrollState.SCROLL)
    private val snapHelperLarge = CustomLinearSnapHelper()
    private val snapHelperSmall = CustomLinearSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view_large.addItemDecoration(CenterItemDecoration())
        recycler_view_small.addItemDecoration(CenterItemDecoration())

        recycler_view_small.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler_view_small.adapter = Adapter(R.layout.item_small)

        recycler_view_large.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler_view_large.adapter = Adapter(R.layout.item_large)

        view_foreground.setOnTouchListener(object : View.OnTouchListener {

            private var timer: Timer? = Timer()

            private var isLarge = false

            override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
                motionEvent ?: return false

                Log.d("TOUCH_EVENT", motionEvent.toString())

                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    timer?.cancel()
                    timer = Timer()
                    timer?.schedule(TimeUnit.SECONDS.toMillis(1)) {
                        runOnUiThread {
//                            initForLarge()
                            TransitionManager.beginDelayedTransition(root)
                            recycler_view_large.bringToFront()
                            isLarge = true
                        }
                    }
                } else {
                    timer?.cancel()
                }

                if (motionEvent.action == MotionEvent.ACTION_UP) {
//                    if (isLarge) {
//                        initForSmall()
                        TransitionManager.beginDelayedTransition(root)
                        recycler_view_small.bringToFront()
//                        isLarge = false
//                    }
                }

                view_foreground.bringToFront()

                return recycler_view_small.dispatchTouchEvent(motionEvent)
            }
        })

        CenterHelper.moveViewToCenter(recycler_view_small, 10)
        CenterHelper.moveViewToCenter(recycler_view_large, 10)

        initForSmall()
    }

    private fun initForSmall() {
//        centerHelperLarge.dettachFromRecyclerView(recycler_view_large)
//        centerHelperSmall.dettachFromRecyclerView(recycler_view_small)
//        recycler_view_small.onFlingListener = null
//        recycler_view_large.onFlingListener = null

        snapHelperSmall.attachToRecyclerView(recycler_view_small)
//        snapHelperLarge.attachToRecyclerView(recycler_view_large)

        centerHelperSmall.attachToRecyclerView(recycler_view_small) {
            CenterHelper.moveViewToCenter(recycler_view_large, it)
        }

        centerHelperLarge.attachToRecyclerView(recycler_view_large)
    }

    private fun initForLarge() {
        centerHelperLarge.dettachFromRecyclerView(recycler_view_large)
        centerHelperSmall.dettachFromRecyclerView(recycler_view_small)
        recycler_view_small.onFlingListener = null
        recycler_view_large.onFlingListener = null

        snapHelperLarge.attachToRecyclerView(recycler_view_large)

        centerHelperLarge.attachToRecyclerView(recycler_view_large) {
            CenterHelper.moveViewToCenter(recycler_view_small, it)
        }

        centerHelperSmall.attachToRecyclerView(recycler_view_small)

    }

}

class Adapter(private val itemType: Int) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int) = itemType

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        ViewHolder(LayoutInflater.from(p0.context).inflate(p1, p0, false))

    override fun getItemCount() = 20

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.textView.text = p1.toString()
    }

    override fun getItemId(position: Int) = position.toLong()

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView as TextView

}


//Timer()
//.apply {
//    schedule(TimeUnit.SECONDS.toMillis(5)) {
//        runOnUiThread {
//            adapter.itemType = R.layout.item_large
//            adapter.notifyItemChanged(0, 20)
//        }
//    }
//}
//.apply {
//    schedule(TimeUnit.SECONDS.toMillis(10)) {
//        runOnUiThread {
//            adapter.itemType = R.layout.item_small
//            adapter.notifyItemChanged(0, 20)
//        }
//    }
//}