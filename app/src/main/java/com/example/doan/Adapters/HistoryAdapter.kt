package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.doan.DataSource.*
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.R
import com.example.doan.databinding.DateItemBinding
import com.example.doan.databinding.PaymentItemBinding
import com.example.doan.databinding.TopupItemBinding
import com.example.doan.databinding.TransferItemBinding
import com.example.doan.databinding.WithdrawitemBinding
import com.example.doan.databinding.WithdrawresultBinding

class HistoryAdapter(
    private var mListData: ArrayList<BalanceChanges>,
    var mcallback: HistoryItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var BalanceChangesListFiltered = mListData
    lateinit var context : Context
    constructor() : this(ArrayList(), object : HistoryItemClickListener{
        override fun onHistoryItemClick(pos: Int) {
            TODO("Not yet implemented")
        }

        override fun onNotificationItemClick(pos: Int) {
            TODO("Not yet implemented")
        }
    })
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        return when (viewType) {
            BalanceChanges.TYPE_DATE -> DateViewHolder(DateItemBinding.inflate(layoutInflater))
            BalanceChanges.TYPE_TRANSFER -> TransferViewHolder(
                TransferItemBinding.inflate(
                    layoutInflater
                )
            )
            BalanceChanges.TYPE_TOPUP -> TopUpViewHolder(TopupItemBinding.inflate(layoutInflater))
            BalanceChanges.TYPE_TRANSFERFROM -> TransferFromViewHolder(
                TransferItemBinding.inflate(
                    layoutInflater
                )
            )
            BalanceChanges.TYPE_WITHDRAW -> WithDrawViewHolder(
                WithdrawitemBinding.inflate(
                    layoutInflater
                )
            )
            else -> {
                PaymentViewHolder(PaymentItemBinding.inflate(layoutInflater))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return BalanceChangesListFiltered[position].type
    }

    fun getfiltereddata(): ArrayList<BalanceChanges> {
        return BalanceChangesListFiltered
    }

    override fun getItemCount(): Int {
        return BalanceChangesListFiltered.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            BalanceChanges.TYPE_TRANSFER -> (holder as TransferViewHolder).bind(
                item = BalanceChangesListFiltered[position] as Transfer
            )
            BalanceChanges.TYPE_TRANSFERFROM -> (holder as TransferFromViewHolder).bind(
                item = BalanceChangesListFiltered[position] as TransferFrom
            )
            BalanceChanges.TYPE_TOPUP -> (holder as TopUpViewHolder).bind(
                item = BalanceChangesListFiltered[position] as TopUp
            )
            BalanceChanges.TYPE_PAYMENT -> (holder as PaymentViewHolder).bind(
                item = BalanceChangesListFiltered[position] as Payment
            )
            BalanceChanges.TYPE_DATE -> (holder as DateViewHolder).bind(
                item = BalanceChangesListFiltered[position] as DateItem
            )
            BalanceChanges.TYPE_WITHDRAW -> (holder as WithDrawViewHolder).bind(
                item = BalanceChangesListFiltered[position] as WithDraw
            )
        }
    }

    inner class DateViewHolder(val binding: DateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem) {
            binding.txtdate.text = item.date.substring(0,10)
        }
    }

    inner class TransferViewHolder(val binding: TransferItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: Transfer) {
            binding.txtHistoryName.text = item.receivername
            binding.txttransfertime.text = item.date.substring(0,10)
            binding.txthistransamount.text = "-"+item.amount.toString()+"đ"
        }
        init{
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class TransferFromViewHolder(val binding: TransferItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: TransferFrom) {
            binding.txttransfer.text = "Transfer From"
            binding.imghistorytransfer.setImageResource(R.drawable.transferfromiocn)
            binding.txtHistoryName.text = item.sendername
            binding.txttransfertime.text = item.date.substring(0,10)
            binding.txthistransamount.text = "+" + item.amount.toString()+"đ"
            binding.txthistransamount.setTextColor(context.resources.getColor(R.color.green))
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class TopUpViewHolder(val binding: TopupItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: TopUp) {
            binding.txttopupsource.text = item.bank
            binding.txttptime.text = item.date.substring(0,10)
            binding.txttpamount.text = "+" + item.amount.toString()+"đ"
            binding.txttpamount.setTextColor(context.resources.getColor(R.color.green))
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class WithDrawViewHolder(val binding: WithdrawitemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: WithDraw) {
            binding.txtwithdrawdestination.text = item.bank
            binding.txtwdbanknumber.text = item.banknumber
            binding.txtwdtime.text = item.date.substring(0,10)
            binding.txtwdamount.text = "-" +item.amount.toString()
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    inner class PaymentViewHolder(val binding: PaymentItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: Payment) {
            binding.txtPaymentOdersn.text = item.odersn
            binding.txtpmtime.text = item.date.substring(0,10)
            binding.txtpmamount.text = "-" +item.amount.toString()
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint != null) {
                    if(constraint.isEmpty()){
                        BalanceChangesListFiltered = mListData
                    }
                    else{
                        val filterlist = ArrayList<BalanceChanges>()
                        for(balanceChange in mListData){
                            if(balanceChange.referenceid.lowercase().contains(constraint.toString().lowercase()) || balanceChange.date.lowercase().contains(constraint.toString().lowercase())){
                                filterlist.add(balanceChange)
                            }
                        }
                        BalanceChangesListFiltered = filterlist
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = BalanceChangesListFiltered
                return filterResults
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    BalanceChangesListFiltered = results.values as ArrayList<BalanceChanges>
                    notifyDataSetChanged()
                }
            }

        }
    }

}