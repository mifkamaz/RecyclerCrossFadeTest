package ru.mifkamaz.recyclercrossfadetest.adapters

import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.mifkamaz.recyclercrossfadetest.FormatFactory
import ru.mifkamaz.recyclercrossfadetest.R
import ru.mifkamaz.recyclercrossfadetest.visibleOrGone
import ru.mifkamaz.recyclercrossfadetest.visibleOrInvisible
import java.util.*

data class Item(
    val price: Long,
    val percent: Int,
    val calendar: Calendar,
    val empty: Boolean,
    var selected: Boolean
)

abstract class BaseAdapter<T : BaseViewHolder>(
    protected var items: List<Item> = ArrayList()
) : RecyclerView.Adapter<T>() {

    override fun onBindViewHolder(holder: T, position: Int) {
        val item = items[position]

        holder.textDate.text = formatDate(item.calendar)
        holder.textPrice.text = FormatFactory.currency.format(item.price)

        setupPercent(holder.percentHeader, item.percent)
        setupTypeface(holder.textDate, item.selected)
        setupTextColor(holder.textDate, item.calendar)
        setupPercentColor(holder, item.selected)

        holder.percentCenter.visibleOrInvisible(!item.empty)

        holder.textPrice.visibleOrGone(!item.empty)
    }

    abstract fun formatDate(calendar: Calendar): String

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = items[position].calendar.timeInMillis

    private fun setupTypeface(textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            Typeface.BOLD
        } else {
            Typeface.NORMAL
        }.also { typeface ->
            textView.setTypeface(null, typeface)
        }
    }

    private fun setupTextColor(textView: TextView, calendar: Calendar) {
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY, Calendar.SATURDAY -> R.color.date_color_friday
            else -> R.color.date_color
        }.let { colorRes ->
            textView.context.resources.getColor(colorRes)
        }.also { color ->
            textView.setTextColor(color)
        }

    }

    private fun setupPercent(view: View, percent: Int) {
        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.matchConstraintPercentHeight = percent.toFloat() / 100
        view.layoutParams = layoutParams
    }

    private fun setupPercentColor(holder: BaseViewHolder, isSelected: Boolean) {
        if (isSelected) {
            R.drawable.item_background_selected
        } else {
            R.drawable.item_background_default
        }.also { background ->
            holder.percentHeader.setBackgroundResource(background)
            holder.percentCenter.setBackgroundResource(background)
            holder.percentFooter.setBackgroundResource(background)
        }
    }

}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textDate: TextView = itemView.findViewById(R.id.text_day)
    val textPrice: TextView = itemView.findViewById(R.id.text_price)
    val percentHeader: View = itemView.findViewById(R.id.view_percent)
    val percentCenter: View = itemView.findViewById(R.id.view_percent_begin)
    val percentFooter: View = itemView.findViewById(R.id.view_percent_null)

}