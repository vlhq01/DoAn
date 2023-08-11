package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.ClicklistenerInterface.PaymentMethodOnClickListener
import com.example.doan.DataSource.BalanceChanges
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.databinding.PaymentMethodrvItemBinding

class PaymentMethodAdapter(
    private var mListData: ArrayList<LinkedBanks>,
    var mcallback: PaymentMethodOnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    inner class PaymentMethosViewHolder(val binding: PaymentMethodrvItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {

        fun bind(item: LinkedBanks) {
            Glide.with(context).load(item.imgurl).into(binding.imgbankmethod)
            binding.txtbankmethosname.text = item.bankname
            binding.txtaccountnumber.text = item.banknumber
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onPaymentMethodClickListener(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        return PaymentMethosViewHolder(PaymentMethodrvItemBinding.inflate(layoutInflater))
    }


    override fun getItemCount(): Int {
        return mListData.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PaymentMethosViewHolder).bind(mListData[position])
    }
}