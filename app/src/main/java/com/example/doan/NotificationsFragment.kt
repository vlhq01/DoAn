package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.HistoryAdapter
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.DataSource.*
import com.example.doan.databinding.HistoryNotificationsFragmentsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationsFragment : Fragment() {
    private val db = Firebase.firestore
    private var _binding: HistoryNotificationsFragmentsBinding?=null
    private val binding get() = _binding!!

    val consolidatedlist = mutableListOf<BalanceChanges>()
    //var bllisttosort = mutableListOf<BalanceChangesSort>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryNotificationsFragmentsBinding.inflate(inflater,container,false)
        val view = binding.root
        //val bl1 = Transfer(20000,"2023-07-09","123243","Hung","dsfrfer5t456")
        //val bl2 = Transfer(40000,"2023-07-08","76456783","Huy","fgdgrtggth")
        //val bl3 = TopUp("2023-07-07",100000, "BIDV", "3546546","dsfertg453344")
        //val bl4 = Payment("2023-07-08",30000,"345436546","sfeerwf454r4")

           // bllisttosort = mutableListOf<BalanceChangesSort>(
            //BalanceChangesSort(bl1,bl1.date),
            //BalanceChangesSort(bl2,bl2.date),
            //BalanceChangesSort(bl3,bl3.date),
            //BalanceChangesSort(bl4,bl4.date),
        //)
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel
        //getfromdb()
        historyNotificationViewModel.bllisttosort.observe(viewLifecycleOwner, Observer {
            val groupedMap : Map<String, List<BalanceChangesSort>> = it.groupBy { it.date }

            val sortedMap = groupedMap.toSortedMap(compareByDescending { it })

            for(date:String in sortedMap.keys){
                consolidatedlist.add(DateItem(date))

                val groupItems: List<BalanceChangesSort>? = groupedMap[date]
                groupItems?.forEach {
                    consolidatedlist.add(it.balancechanges)
                }

            }
            binding.rvNotiHistory.adapter = HistoryAdapter(consolidatedlist as ArrayList<BalanceChanges>,historyitemclicklistener)
        })

        binding.rvNotiHistory.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)

        return view
    }

    /*fun getfromdb(){

        val user = Firebase.auth
        val uuid = user.currentUser?.uid
        if (uuid != null) {
            db.collection("users").document(uuid).collection("Wallet Balance Changes").get().addOnSuccessListener {
                documents ->
                for(doc in documents){
                    var type  = doc.getLong("type")
                    Log.d(TAG, "TYPE: " +type)
                    if (type != null) {
                        when(type.toInt()){
                            BalanceChanges.TYPE_TRANSFER -> {
                                val transfer = doc.toObject(Transfer::class.java)
                                bllisttosort.add(BalanceChangesSort(transfer,transfer.date))
                            }
                            BalanceChanges.TYPE_TOPUP -> {
                                val topup = doc.toObject(TopUp::class.java)
                                bllisttosort.add(BalanceChangesSort(topup,topup.date))
                            }
                            BalanceChanges.TYPE_PAYMENT -> {
                                val payment = doc.toObject(Payment::class.java)
                                bllisttosort.add(BalanceChangesSort(payment,payment.date))
                            }
                        }
                    }
                }
                Log.d(TAG, "getfromdbsfw43r34r34rwef: " + bllisttosort.toString())
                val groupedMap : Map<String, List<BalanceChangesSort>> = bllisttosort.groupBy { it.date }

                val sortedMap = groupedMap.toSortedMap(compareByDescending { it })

                for(date:String in sortedMap.keys){
                    consolidatedlist.add(DateItem(date))

                    val groupItems: List<BalanceChangesSort>? = groupedMap[date]
                    groupItems?.forEach {
                        consolidatedlist.add(it.balancechanges)

                    }
                }
                binding.rvNotiHistory.adapter = HistoryAdapter(consolidatedlist as ArrayList<BalanceChanges>,historyitemclicklistener)
            }.addOnFailureListener{
                Log.d(TAG, "getfromdb: some thing is wrong")
            }
        }
    }*/

    val historyitemclicklistener = object : HistoryItemClickListener {
        override fun onHistoryItemClick(pos: Int) {
            val type = consolidatedlist[pos].type
            when(type){
                BalanceChanges.TYPE_TRANSFER -> {
                    val action = NotificationsFragmentDirections.actionNotificationsFragmentToTransferResultFragment(consolidatedlist[pos] as Transfer)
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_TRANSFERFROM -> {
                    val action = NotificationsFragmentDirections.actionNotificationsFragmentToTransferFromResultFragment(consolidatedlist[pos] as TransferFrom)
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_TOPUP -> {
                    val action = NotificationsFragmentDirections.actionNotificationsFragmentToTopUpResultFragment(consolidatedlist[pos] as TopUp)
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_PAYMENT -> {
                    val action = NotificationsFragmentDirections.actionNotificationsFragmentToPaymentResultFragment(consolidatedlist[pos] as Payment)
                    findNavController().navigate(action)
                }
            }
        }
    }
}