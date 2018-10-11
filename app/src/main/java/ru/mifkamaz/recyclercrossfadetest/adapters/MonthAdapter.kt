package ru.mifkamaz.recyclercrossfadetest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.mifkamaz.recyclercrossfadetest.R
import java.util.*

class MonthAdapter(private val months: List<Int>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int) = R.layout.item_with_match

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        ViewHolder(LayoutInflater.from(p0.context).inflate(p1, p0, false))

    override fun getItemCount() = months.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, months[p1])
        p0.textView.text = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toLowerCase()
    }

    override fun getItemId(position: Int) = position.toLong()

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView as TextView

}
