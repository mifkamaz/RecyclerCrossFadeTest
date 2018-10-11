package ru.mifkamaz.recyclercrossfadetest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.mifkamaz.recyclercrossfadetest.adapters.Item
import ru.mifkamaz.recyclercrossfadetest.adapters.LargeAdapter
import ru.mifkamaz.recyclercrossfadetest.adapters.MonthAdapter
import ru.mifkamaz.recyclercrossfadetest.adapters.SmallAdapter
import java.util.*

class MainActivity : AppCompatActivity() {

    private var isMonthRecyclerMoving = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstRecycler = findViewById<RecyclerView>(R.id.recycler_view_large)
        val secondRecycler = findViewById<RecyclerView>(R.id.recycler_view_small)
        val monthRecycler = findViewById<RecyclerView>(R.id.recycler_view_month)

        val items = getData().map {
            Item(
                it.price,
                it.percent.toInt(),
                it.d(),
                it.isEmpty(),
                it.d().isToday()
            )
        }
        val months = getMonths()

        secondRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        secondRecycler.adapter = SmallAdapter(items)


        firstRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        firstRecycler.adapter = LargeAdapter(items) { newSelectedPosition ->
            if (items[newSelectedPosition].empty) {
                return@LargeAdapter
            }

            val oldSelected = items.find { it.selected } ?: return@LargeAdapter
            val newSelected = items[newSelectedPosition]

            oldSelected.selected = false
            newSelected.selected = true

            firstRecycler.adapter?.notifyItemChanged(items.indexOf(oldSelected))
            firstRecycler.adapter?.notifyItemChanged(items.indexOf(newSelected))

            secondRecycler.adapter?.notifyItemChanged(items.indexOf(oldSelected))
            secondRecycler.adapter?.notifyItemChanged(items.indexOf(newSelected))

            CenterHelper.moveViewToCenter(secondRecycler, newSelectedPosition, false)
        }

        RecyclerViewCrossFadeController(
            findViewById(R.id.view_foreground),
            firstRecycler,
            secondRecycler,
            items.indexOf(items.find { it.selected })
        )

        monthRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        monthRecycler.adapter = MonthAdapter(months)
        CustomLinearSnapHelper().attachToRecyclerView(monthRecycler)

        CenterHelper(CenterHelper.ScrollState.SCROLL, false)
            .attachToRecyclerView(secondRecycler) {
                if (isMonthRecyclerMoving) {
                    return@attachToRecyclerView
                }

                val currentPosition =
                    CenterHelper.findCenterViewPosition(monthRecycler, false)
                        ?: return@attachToRecyclerView

                val newPosition = months.indexOf(items[it].calendar.get(Calendar.MONTH))

                monthRecycler.smoothScrollBy((newPosition - currentPosition) * monthRecycler.width, 0)
            }

        monthRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    isMonthRecyclerMoving = newState != RecyclerView.SCROLL_STATE_IDLE
                }

            }
        )
    }

    private fun getData(): List<DayStatics> =
        Gson()
            .fromJson(
                JsonExample.JSON_EXAMPLE,
                TypeToken.getParameterized(
                    ArrayList::class.java,
                    DayStatics::class.java
                ).type
            ) as List<DayStatics>

    private fun getMonths(): List<Int> = getData()
        .groupBy {
            it.d().get(Calendar.MONTH)
        }
        .keys
        .toList()

}

fun Calendar.isToday(): Boolean {
    val today = Calendar.getInstance()
    return get(Calendar.YEAR) == today.get(Calendar.YEAR) && get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
}

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}


