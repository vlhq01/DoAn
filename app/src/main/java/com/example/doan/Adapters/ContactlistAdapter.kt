package com.example.doan.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.ContactsListFragment
import com.example.doan.ContactsListFragmentDirections
import com.example.doan.DataSource.Contacts
import com.example.doan.MainFragmentDirections
import com.example.doan.R
import kotlinx.coroutines.NonDisposableHandle.parent

class ContactlistAdapter(
    private val context: ContactsListFragment,
    private val contactsModalArrayList: ArrayList<Contacts>?
) : RecyclerView.Adapter<ContactlistAdapter.contactItemViewHolder>() {

    class contactItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.imgavadefault)
        val textViewName : TextView = view.findViewById(R.id.txtReceiverName)
        val textViewNumber : TextView = view.findViewById(R.id.txtPhoneNumber)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): contactItemViewHolder {
        val adapterLayout = LayoutInflater.from(context.context)
            .inflate(R.layout.moneytransfer_contactlist_item, parent, false)
        return contactItemViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: contactItemViewHolder, position: Int) {
        val item = contactsModalArrayList?.get(position)
        if (item != null) {
            holder.textViewName.text = item.userName
        }
        if (item != null) {
            holder.textViewNumber.text = item.contactNumber
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            val action = item?.userName?.let { it1 ->
                item.contactNumber?.let { it2 ->
                   ContactsListFragmentDirections.actionContactsListFragmentToMoneyTransferFragment(
                       it1,it2
                   )
                }
            }
            if (action != null) {
                holder.itemView.findNavController().navigate(action)
            }
        })
    }



    override fun getItemCount(): Int {
        return contactsModalArrayList?.size ?:0
    }


}