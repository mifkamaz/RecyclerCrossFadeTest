package ru.mifkamaz.recyclercrossfadetest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mifkamaz.recyclercrossfadetest.R
import ru.mifkamaz.recyclercrossfadetest.visibleOrInvisible
import java.util.*

class SmallAdapter(
    items: List<Item>
) : BaseAdapter<DayViewHolder>(items) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        DayViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_day_static_small,
                p0,
                false
            )
        )

    override fun formatDate(calendar: Calendar): String = calendar.get(Calendar.DAY_OF_MONTH).toString()

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.textPrice.visibleOrInvisible(items[position].selected)
    }

}

class DayViewHolder(itemView: View) : BaseViewHolder(itemView)