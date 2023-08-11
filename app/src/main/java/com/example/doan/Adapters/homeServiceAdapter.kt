package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.DataSource.service
import com.example.doan.ClicklistenerInterface.HomeServiceItemClickListener
import com.example.doan.R


class homeServiceAdapter(
    private var mListData: ArrayList<service>,
    var mcallback: HomeServiceItemClickListener
) : RecyclerView.Adapter<homeServiceAdapter.serviceItemViewHolder>() {

    private var mContext: Context? = null


    fun setCallback(callback: HomeServiceItemClickListener) {
        this.mcallback = callback
    }

    fun HomeServiceAdapter(listData: ArrayList<service>?) {
        if (listData != null) {
            mListData = listData
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): serviceItemViewHolder {
        mContext = parent.context
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.homeservice_recycler_item, parent, false)
        return serviceItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: serviceItemViewHolder, position: Int) {
        val item = mListData!![position]
        holder.tvTitle.text = item.serviceName
        holder.imgService.setImageResource(item.imageResourceId)

    }


    override fun getItemCount(): Int {
        return if (mListData != null) mListData!!.size else 0
    }

    inner class serviceItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnClickListener {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var imgService: ImageView = itemView.findViewById(R.id.imgservice)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onItemClick(position)
        }


    }


}
