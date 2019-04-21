package com.example.gallery

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.info_fragment.view.*
import kotlinx.android.synthetic.main.photos_list_item.view.*


class MyAdapter(
    private val items: ArrayList<Photo>, private val context: Context,
    val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.photos_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Photo = items[position]
        holder.itemView.setOnClickListener { clickListener(position) }
        holder.ivPhoto.setImageURI(Uri.parse(item.image))
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPhoto = view.ivPhoto!!
}