package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.doan.ClicklistenerInterface.ContactsListItemClickListener

import com.example.doan.DataSource.Contacts

import com.example.doan.R
import com.example.doan.databinding.MoneytransferContactlistItemBinding


class ContactlistAdapter(
    var mcallback: ContactsListItemClickListener,
    private val contactsModalArrayList: ArrayList<Contacts>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context

    inner class contactItemViewHolder(val binding: MoneytransferContactlistItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        OnClickListener {
        fun bind(item: Contacts) {
            binding.imgavadefault.setImageDrawable(context.getDrawable(R.drawable.orangeavaicon))
            binding.txtReceiverName.text = item.name
            binding.txtPhoneNumber.text = item.phone
        }

        init {
            binding.root.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onContactsListItemItemClick(position)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): contactItemViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return contactItemViewHolder(MoneytransferContactlistItemBinding.inflate(layoutInflater))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        contactsModalArrayList?.get(position)?.let { (holder as contactItemViewHolder).bind(it) }

    }


    override fun getItemCount(): Int {
        return contactsModalArrayList?.size ?: 0
    }


}