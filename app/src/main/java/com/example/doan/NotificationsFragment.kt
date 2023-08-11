package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.HistoryAdapter
import com.example.doan.Adapters.NotificationAdapter
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.DataSource.*
import com.example.doan.databinding.HistoryNotificationsFragmentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationsFragment : Fragment() {
    private val db = Firebase.firestore
    private var _binding: HistoryNotificationsFragmentsBinding? = null
    private val binding get() = _binding!!
    val notificationlist = mutableListOf<BalanceChangesWithCurrentBalance>()
    val consolidatedlist = mutableListOf<BalanceChanges>()

    override fun onResume() {
        super.onResume()
        binding.historynotificationtablayout.getTabAt(1)?.select()
        binding.historynotificationtablayout.getTabAt(0)?.select()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryNotificationsFragmentsBinding.inflate(inflater, container, false)
        val view = binding.root
        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (navBar != null) {
            navBar.isVisible = true
        }
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
        val mainViewModel = (activity as MainActivity).mainViewModel
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel
        //getfromdb()


        mainViewModel.balance.value?.let {
            BalanceChangesWithCurrentBalance(
                Transfer(100000, "2023-10-12", "324534563456", "Long", "2345346356"), "53453454543",
                it
            )
        }?.let { notificationlist.add(it) }
        binding.historynotificationtablayout.addOnTabSelectedListener(object :
            OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> historyNotificationViewModel.bllisttosort.observe(
                            viewLifecycleOwner,
                            Observer {
                                val groupedMap: Map<String, List<BalanceChangesSort>> =
                                    it.groupBy { it.date }

                                val sortedMap = groupedMap.toSortedMap(compareByDescending { it })

                                for (date: String in sortedMap.keys) {
                                    consolidatedlist.add(DateItem(date))

                                    val groupItems: List<BalanceChangesSort>? = groupedMap[date]
                                    groupItems?.forEach {
                                        consolidatedlist.add(it.balancechanges)
                                    }

                                }
                                binding.rvNotiHistory.adapter = HistoryAdapter(
                                    consolidatedlist as ArrayList<BalanceChanges>,
                                    historyitemclicklistener
                                )
                            })

                        1 -> binding.rvNotiHistory.adapter = NotificationAdapter(
                            notificationlist as ArrayList<BalanceChangesWithCurrentBalance>,
                            historyitemclicklistener
                        )

                    }
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }


            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.rvNotiHistory.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        return view
    }


    val historyitemclicklistener = object : HistoryItemClickListener {
        override fun onHistoryItemClick(pos: Int) {
            val type = consolidatedlist[pos].type
            when (type) {
                BalanceChanges.TYPE_TRANSFER -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToTransferResultFragment(
                            consolidatedlist[pos] as Transfer
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_TRANSFERFROM -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToTransferFromResultFragment(
                            consolidatedlist[pos] as TransferFrom
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_TOPUP -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToTopUpResultFragment(
                            consolidatedlist[pos] as TopUp
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_PAYMENT -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToPaymentResultFragment(
                            consolidatedlist[pos] as Payment
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_WITHDRAW -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToWithDrawResuktFragment(
                            consolidatedlist[pos] as WithDraw
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }
}