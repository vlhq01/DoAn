package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan.ClicklistenerInterface.WithDrawBankListItemClicklistener
import com.example.doan.DataSource.LinkedBanks

import com.example.doan.databinding.WithdrawlinkedbankitemBinding
import java.util.ArrayList

class WithdrawBankListAdapter(
    private val mlistdata: ArrayList<LinkedBanks>?,
    private val mCallBack: WithDrawBankListItemClicklistener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    inner class WithdrawBankListViewHolder(val binding: WithdrawlinkedbankitemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: LinkedBanks) {
            Glide.with(context).load(item.imgurl).into(binding.imgwithdrawbank)
            binding.txtwithdrawbankname.text = item.bankname
            binding.txtwithdrawbanknumber.text = item.banknumber
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mCallBack.onWithDrawBanklistItemClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return WithdrawBankListViewHolder(WithdrawlinkedbankitemBinding.inflate(layoutInflater))
    }


    override fun getItemCount(): Int {
        return mlistdata?.size ?: 0
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mlistdata?.get(position)?.let { (holder as WithdrawBankListViewHolder).bind(it) }
    }
}