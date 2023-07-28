package com.example.doan.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan.BankListFragment
import com.example.doan.ClicklistenerInterface.BanklistItemClickListener
import com.example.doan.DataSource.Banks
import com.example.doan.databinding.BankslistitemBinding
import java.util.ArrayList

class BankLinkingAdapter(private val context: BankListFragment,
                         private val dataset: ArrayList<Banks>?, var  mcallback : BanklistItemClickListener
) : RecyclerView.Adapter<BankLinkingAdapter.bankLinkingViewHolder>() {

    inner class bankLinkingViewHolder(val binding : BankslistitemBinding) : RecyclerView.ViewHolder(binding.root), OnClickListener {

        fun bind(item : Banks){
            Glide.with(context).load(item.imgUrl).into(binding.imgBanks)
            binding.tvBanksName.text = item.bankname

        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val pos = bindingAdapterPosition
            mcallback.onBanklistItemClick(pos)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankLinkingAdapter.bankLinkingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return bankLinkingViewHolder(BankslistitemBinding.inflate(layoutInflater))
    }


    override fun onBindViewHolder(holder: bankLinkingViewHolder, position: Int) {
        holder.bind(item = dataset?.get(position) as Banks)
    }


    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }




}