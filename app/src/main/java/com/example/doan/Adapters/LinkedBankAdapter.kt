package com.example.doan.Adapters

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
import java.util.ArrayList


class LinkedBankAdapter(private val context: WalletFragment,
                        private val dataset: ArrayList<LinkedBanks>?
) : RecyclerView.Adapter<LinkedBankAdapter.LinkedBankViewHolder>() {

    class LinkedBankViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.imglinkedbank)
        val banksName: TextView = view.findViewById(R.id.txtlinkedbankname)
        val banksNumber: TextView = view.findViewById(R.id.txtlinkedbanknumber)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkedBankViewHolder {
        val adapterLayout = LayoutInflater.from(context.context)
            .inflate(R.layout.linkedbankrv_item, parent, false)
        return LinkedBankViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: LinkedBankViewHolder, position: Int) {
        val item = dataset?.get(position)
        if (item != null) {
            Glide.with(context).load(item.imgurl).into(holder.imageView)
        }
        if (item != null) {
            holder.banksName.setText(item.bankname)
        }
        if (item != null) {
            holder.banksNumber.setText(item.banknumber)
        }
    }


    override fun getItemCount(): Int {
        return dataset?.size ?: 0
    }




}

