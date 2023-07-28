package com.example.doan.Adapters

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.ClicklistenerInterface.MobileItemClickListener
import com.example.doan.DataSource.Mobile
import com.example.doan.R
import com.example.doan.databinding.MobileRecyclerviewItemBinding

import java.text.DecimalFormat


class MobileAdapter(private val mListData: ArrayList<Mobile>, val mCallback:MobileItemClickListener) : RecyclerView.Adapter<MobileAdapter.MobileItemViewHolder>() {
    var selectedItemPos = -1
    var lastItemSelectedPos = -1
    lateinit var context : Context
    inner class MobileItemViewHolder(val binding: MobileRecyclerviewItemBinding) :RecyclerView.ViewHolder(binding.root), OnClickListener {
       fun bind(item : Mobile){
           binding.txtmobilevalue.text = item.value.toString()
           binding.txtmobilediscount.text = item.discount.toString()
           val priceafterdiscount = item.value - (item.value * (item.discount.toDouble()/100))
           val df = DecimalFormat("#,###")
           val stringtospan = SpannableString("Pay: đ"+ df.format(priceafterdiscount.toInt()).toString())
           stringtospan.setSpan(UnderlineSpan(),5,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
           binding.txtmobilefinalprice.text = stringtospan
       }

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

        fun defaultBg() {
            binding.mobileitemcardview.setStrokeColor(context.resources.getColor(R.color.gray_white))
            binding.txtmobilevalue.setTextColor(context.resources.getColor(R.color.grey))
        }

        fun selectedBg() {
            binding.mobileitemcardview.setStrokeColor(context.resources.getColor(R.color.orange))
            binding.txtmobilevalue.setTextColor(context.resources.getColor(R.color.orange))
        }

        override fun onClick(v: View?) {
            val pos = bindingAdapterPosition
            mCallback.onMobileItemItemClick(pos)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MobileItemViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return  MobileItemViewHolder(MobileRecyclerviewItemBinding.inflate(layoutInflater))
    }


    override fun onBindViewHolder(holder: MobileItemViewHolder, position: Int) {
        holder.bind(item = mListData[position])
        if(position == selectedItemPos)
            holder.selectedBg()
        else {
            holder.defaultBg()
            holder.bind(item = mListData[position])
        }
    }


    override fun getItemCount(): Int {
        return mListData.size
    }
}