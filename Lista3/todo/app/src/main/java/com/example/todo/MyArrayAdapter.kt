package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.ScrollingTabContainerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyArrayAdapter(context: Context, var data: ArrayList<ListItem>) :
    ArrayAdapter<ListItem>(context, R.layout.list_item, data) {

    private class ViewHolder(row: View?) {
        var tvTask: TextView? = null
        var tvDate: TextView? = null
        var tvTime: TextView? = null
        var tvPrior: TextView? = null
        var tvImage: ImageView? = null

        init {
            this.tvTask = row?.findViewById(R.id.tvTask)
            this.tvDate = row?.findViewById(R.id.tvDate)
            this.tvTime = row?.findViewById(R.id.tvTime)
            this.tvPrior = row?.findViewById(R.id.tvPrior)
            this.tvImage = row?.findViewById(R.id.tvImage)
        }
    }

    private val images: IntArray = intArrayOf(
        R.drawable.ic_action_other,
        R.drawable.ic_action_home,
        R.drawable.ic_action_work,
        R.drawable.ic_action_shopping,
        R.drawable.ic_action_date
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_item, parent, false)

            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view?.tag as ViewHolder
        }
        viewHolder.tvTask?.text = data[position].task
        setDateTime(viewHolder.tvDate, data[position].date)
        setDateTime(viewHolder.tvTime, data[position].time)
        setPrior(viewHolder.tvPrior, data[position].prior)
        setImage(viewHolder.tvImage, data[position].image)
        return view as View
    }

    private fun setDateTime(tv: TextView?, td: String) {
        if (td == "none") {
            tv?.text = ""
        } else {
            tv?.text = td
        }
    }

    private fun setImage(tvImage: ImageView?, image: String) {
        when (image) {
            "other" -> tvImage?.setImageResource(images[0])
            "home" -> tvImage?.setImageResource(images[1])
            "work" -> tvImage?.setImageResource(images[2])
            "shopping" -> tvImage?.setImageResource(images[3])
            "date" -> tvImage?.setImageResource(images[4])
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPrior(tvPrior: TextView?, prior: Int) {
        when (prior) {
            0 -> {
                tvPrior?.text = "normal"
                tvPrior?.setTextColor(Color.parseColor("#000000"))
            }
            1 -> {
                tvPrior?.text = "important"
                tvPrior?.setTextColor(Color.parseColor("#FFA500"))
            }
            2 -> {
                tvPrior?.text = "very important"
                tvPrior?.setTextColor(Color.parseColor("#FF0000"))
            }
        }
    }
}