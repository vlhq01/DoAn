package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.R
import com.example.doan.WalletFragment
import com.example.doan.databinding.LinkedbankrvItemBinding
import java.util.ArrayList


class LinkedBankAdapter(
    private val mlistdata: ArrayList<LinkedBanks>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    inner class LinkedBankViewHolder(val binding: LinkedbankrvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LinkedBanks) {
            Glide.with(context).load(item.imgurl).into(binding.imglinkedbank)
            binding.txtlinkedbankname.text = item.bankname
            binding.txtlinkedbanknumber.text = item.banknumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return LinkedBankViewHolder(LinkedbankrvItemBinding.inflate(layoutInflater))
    }


    override fun getItemCount(): Int {
        return mlistdata?.size ?: 0
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mlistdata?.get(position)?.let { (holder as LinkedBankViewHolder).bind(it) }
    }


}

