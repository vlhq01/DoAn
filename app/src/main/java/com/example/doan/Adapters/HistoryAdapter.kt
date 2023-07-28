package com.example.doan.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.DataSource.*
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.R
import com.example.doan.databinding.DateItemBinding
import com.example.doan.databinding.PaymentItemBinding
import com.example.doan.databinding.TopupItemBinding
import com.example.doan.databinding.TransferItemBinding

class HistoryAdapter(private var mListData: ArrayList<BalanceChanges>, var  mcallback : HistoryItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType){
              BalanceChanges.TYPE_DATE -> DateViewHolder(DateItemBinding.inflate(layoutInflater))
              BalanceChanges.TYPE_TRANSFER -> TransferViewHolder(TransferItemBinding.inflate(layoutInflater))
              BalanceChanges.TYPE_TOPUP -> TopUpViewHolder(TopupItemBinding.inflate(layoutInflater))
              BalanceChanges.TYPE_TRANSFERFROM -> TransferFromViewHolder(TransferItemBinding.inflate(layoutInflater))
            else -> {
                PaymentViewHolder(PaymentItemBinding.inflate(layoutInflater))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mListData[position].type
    }

    override fun getItemCount(): Int {
        return mListData.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            BalanceChanges.TYPE_TRANSFER -> (holder as TransferViewHolder).bind(
                item = mListData[position] as Transfer
            )
            BalanceChanges.TYPE_TRANSFERFROM -> (holder as TransferFromViewHolder).bind(
                item = mListData[position] as TransferFrom
            )
            BalanceChanges.TYPE_TOPUP -> (holder as TopUpViewHolder).bind(
                item = mListData[position] as TopUp
            )
            BalanceChanges.TYPE_PAYMENT -> (holder as PaymentViewHolder).bind(
                item = mListData[position] as Payment
            )
            BalanceChanges.TYPE_DATE -> (holder as DateViewHolder).bind(
                item = mListData[position] as DateItem
            )
        }
    }

    inner class DateViewHolder(val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem) {
            binding.txtdate.text = item.date
        }
    }

    inner class TransferViewHolder(val binding : TransferItemBinding) : RecyclerView.ViewHolder(binding.root), OnClickListener{
        fun bind(item : Transfer){
            binding.txtHistoryName.text = item.receivername
            binding.txttransfertime.text = item.date
            binding.txthistransamount.text = item.amount.toString()
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class TransferFromViewHolder(val binding : TransferItemBinding) : RecyclerView.ViewHolder(binding.root), OnClickListener{
        fun bind(item : TransferFrom){
            binding.txttransfer.text = "Transfer From"
            binding.imghistorytransfer.setImageResource(R.drawable.transferfromiocn)
            binding.txtHistoryName.text = item.sendername
            binding.txttransfertime.text = item.date
            binding.txthistransamount.text = item.amount.toString()
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class TopUpViewHolder(val binding : TopupItemBinding) : RecyclerView.ViewHolder(binding.root), OnClickListener{
        fun bind(item : TopUp){
            binding.txttopupsource.text = item.bank
            binding.txttptime.text = item.date
            binding.txttpamount.text = item.amount.toString()
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class PaymentViewHolder(val binding : PaymentItemBinding) : RecyclerView.ViewHolder(binding.root), OnClickListener{
        fun bind(item : Payment){
            binding.txtPaymentOdersn.text = item.odersn
            binding.txtpmtime.text = item.date
            binding.txtpmamount.text = item.amount.toString()
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }
}