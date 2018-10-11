package ru.mifkamaz.recyclercrossfadetest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.mifkamaz.recyclercrossfadetest.FormatFactory
import ru.mifkamaz.recyclercrossfadetest.R
import ru.mifkamaz.recyclercrossfadetest.visibleOrGone
import java.util.*

class LargeAdapter(
    items: List<Item>,
    private val onItemClick: (Int) -> Unit
) : BaseAdapter<LargeViewHolder>(items) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        LargeViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_day_static_large,
                p0,
                false
            )
        )

    override fun onBindViewHolder(holder: LargeViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onItemClick(holder.adapterPosition)
        }

        val item = items[position]

        holder.textPriceSecond.visibleOrGone(item.selected || item.empty)
        setupTextPriceSecond(holder.textPriceSecond, item.empty)
    }

    private fun setupTextPriceSecond(textView: TextView, isEmpty: Boolean) {
        textView.text = if (isEmpty) {
            "Нет данных"
        } else {
            "на 1 человека"
        }
    }

    override fun formatDate(calendar: Calendar): String = FormatFactory.ddEE_space_comma.format(calendar.time)

}

class LargeViewHolder(itemView: View) : BaseViewHolder(itemView) {

    val textPriceSecond: TextView = itemView.findViewById(R.id.text_per_tourist)

}