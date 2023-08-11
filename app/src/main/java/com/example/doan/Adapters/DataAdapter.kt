package com.example.doan.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton

import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.adapters.TextViewBindingAdapter.setText

import androidx.recyclerview.widget.RecyclerView
import com.example.doan.ClicklistenerInterface.DataItemClickListener
import com.example.doan.ClicklistenerInterface.MobileItemClickListener
import com.example.doan.DataSource.Data

import com.example.doan.R
import com.example.doan.databinding.DataRecyclerviewItemBinding
import com.example.doan.databinding.DateItemBinding
import java.text.DecimalFormat

class DataAdapter(private val mListData: ArrayList<Data>, val mCallback: DataItemClickListener) :
    RecyclerView.Adapter<DataAdapter.DataItemViewHolder>() {
    lateinit var context: Context
    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    inner class DataItemViewHolder(val binding: DataRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: Data) {
            binding.txtdataprice.text = item.value.toString()
            binding.txtdatadiscount.text = item.discount.toString()
            val priceafterdiscount = item.value - (item.value * (item.discount.toDouble() / 100))
            val df = DecimalFormat("#,###")
            val stringtospan =
                SpannableString("đ" + df.format(priceafterdiscount.toInt()).toString())
            stringtospan.setSpan(UnderlineSpan(), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.txtdatafinalprice.text = stringtospan
            if (item.datacapacity < 1) {
                binding.rbpackofdata.setText(item.networlprovider + " " + (item.datacapacity * 1000).toInt() + " MB" + ", " + item.duration + " days")
            } else {
                binding.rbpackofdata.setText(item.networlprovider + " " + item.datacapacity + " GB" + ", " + item.duration + " days")
            }
            binding.rbpackofdata.setOnClickListener({
                selectedItemPos = bindingAdapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
                onClick(itemView)
            })
        }

        init {
            itemView.setOnClickListener {
                selectedItemPos = bindingAdapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
                onClick(itemView)
            }
        }


        fun defaultRb() {
            binding.rbpackofdata.isChecked = false
            binding.rbpackofdata.setTextColor(context.resources.getColor(R.color.black))
        }

        fun selectedRb() {
            binding.rbpackofdata.isChecked = true
            binding.rbpackofdata.setTextColor(context.resources.getColor(R.color.orange))
        }


        override fun onClick(v: View?) {
            val pos = bindingAdapterPosition
            mCallback.onDataItemItemClick(pos)
        }

    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataItemViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return DataItemViewHolder(DataRecyclerviewItemBinding.inflate(layoutInflater))
    }


    override fun onBindViewHolder(holder: DataItemViewHolder, position: Int) {
        holder.bind(mListData[position])
        if (position == selectedItemPos)
            holder.selectedRb()
        else {
            holder.defaultRb()
            holder.bind(item = mListData[position])
        }
    }


    override fun getItemCount(): Int {
        return mListData.size
    }
}