package com.example.doan.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.DataSource.TopUpValue
import com.example.doan.R
import com.example.doan.ClicklistenerInterface.TopUpValueItemClickListener
import com.example.doan.databinding.TopupvalueItemBinding

class MoneyTopUpAdapter(private var mListData: ArrayList<TopUpValue>, var  mcallback : TopUpValueItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    var selectedItemPos = -1
    var lastItemSelectedPos = -1
    lateinit var context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        return TopUpValueViewHolder(TopupvalueItemBinding.inflate(layoutInflater))
    }


    override fun getItemCount(): Int {
        return mListData.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TopUpValueViewHolder).bind(item = mListData[position])
        if(position == selectedItemPos)
            holder.selectedBg()
        else {
            holder.defaultBg()
            holder.bind(item = mListData[position])
        }





    }

    inner class TopUpValueViewHolder( val binding: TopupvalueItemBinding) : RecyclerView.ViewHolder(binding.root), OnClickListener {

        init {

            itemView.setOnClickListener {
                selectedItemPos = bindingAdapterPosition
                if(lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
                onClick(itemView)
            }
        }

        fun bind(item : TopUpValue){
            binding.txttopupvalue.text = item.value.toString()
        }

        fun defaultBg() {
            binding.topupvaluecardview.setStrokeColor(context.resources.getColor(R.color.gray_white))
           binding.txttopupvalue.setTextColor(context.resources.getColor(R.color.grey))
        }

        fun selectedBg() {
           binding.topupvaluecardview.setStrokeColor(context.resources.getColor(R.color.orange))
           binding.txttopupvalue.setTextColor(context.resources.getColor(R.color.orange))
        }



        override fun onClick(v: View?) {
          val pos = bindingAdapterPosition
            mcallback.onTopUpValueItemClick(pos)
            Log.d(TAG, "onClick: "+ pos)
        }
    }
}