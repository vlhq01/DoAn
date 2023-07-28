package com.example.doan.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.R

class ViewpagerAdapter(private val mListData: ArrayList<Int>) :
    RecyclerView.Adapter<ViewpagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var img: ImageView

        init {
            img = itemView.findViewById<ImageView>(R.id.imgbanner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.banneritem, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currImg = mListData[position]
        holder.img.setImageResource(currImg)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }
}
