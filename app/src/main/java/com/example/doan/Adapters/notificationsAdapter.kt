package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.DataSource.noti
import com.example.doan.NotificationsFragment
import com.example.doan.R

class notificationsAdapter(
    private val context: NotificationsFragment,
    private val dataset: List<noti>) : RecyclerView.Adapter<notificationsAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.imgnoti)
        val textViewType : TextView = view.findViewById(R.id.txttype)
        val textViewHeader : TextView = view.findViewById(R.id.txtheader)
        val textViewContent : TextView = view.findViewById(R.id.txtcontent)
        val textViewDate : TextView = view.findViewById(R.id.txtdate)
        val textViewTime : TextView = view.findViewById(R.id.txttime)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(context.context)
            .inflate(R.layout.notifications_recycle_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }


    override fun getItemCount(): Int {
        return dataset.size
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.imageView.setImageResource(item.imageResourceId)
        holder.textViewType.text = item.notitype
        holder.textViewHeader.text= item.header
        holder.textViewContent.text = item.content
        holder.textViewDate.text = item.timeday
        holder.textViewTime.text = item.timehour
    }
}
