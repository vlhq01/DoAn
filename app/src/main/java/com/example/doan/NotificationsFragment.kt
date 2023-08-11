package com.example.doan


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.Adapters.HistoryAdapter
import com.example.doan.Adapters.NotificationAdapter
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.DataSource.*
import com.example.doan.databinding.HistoryNotificationsFragmentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class NotificationsFragment : Fragment() {
    private var _binding: HistoryNotificationsFragmentsBinding? = null
    private val binding get() = _binding!!
    val notificationlist = mutableListOf<BalanceChangesWithCurrentBalance>()
    val consolidatedlist = mutableListOf<BalanceChanges>()
    lateinit var historyAdapter : HistoryAdapter
    var tabposition = 0
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

        val mainViewModel = (activity as MainActivity).mainViewModel
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel



       getnotificationdata()



        historyAdapter = HistoryAdapter()

        binding.historynotificationtablayout.addOnTabSelectedListener(object :
            OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab != null) {
                    tabposition = tab.position
                    when (tab.position) {
                        0 -> {
                            consolidatedlist.clear()
                            historyNotificationViewModel.bllisttosort.observe(
                                viewLifecycleOwner,
                                Observer {
                                    val groupedMap: Map<String, List<BalanceChangesSort>> =
                                        it.groupBy { it.date.substring(0, 10) }

                                    val sortedMap =
                                        groupedMap.toSortedMap(compareByDescending { it })

                                    for (date: String in sortedMap.keys) {
                                        consolidatedlist.add(DateItem(date))

                                        val groupItems: List<BalanceChangesSort>? = groupedMap[date]
                                        groupItems?.forEach {
                                            consolidatedlist.add(it.balancechanges)
                                        }

                                    }
                                     historyAdapter = HistoryAdapter(
                                        consolidatedlist as ArrayList<BalanceChanges>,
                                        historyitemclicklistener
                                    )
                                    binding.rvNotiHistory.adapter = historyAdapter
                                })
                            }

                        1 -> {
                            val sortednotificationlist = ArrayList(notificationlist.sortedWith(compareBy {
                                it.balanceChanges.date
                            }))
                            sortednotificationlist.reverse()
                            binding.rvNotiHistory.adapter = NotificationAdapter(
                                sortednotificationlist as ArrayList<BalanceChangesWithCurrentBalance>,
                                historyitemclicklistener
                            )
                        }
                    }
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }


            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.hisnotisearchview.setIconifiedByDefault(false)

        binding.hisnotisearchview.setOnQueryTextListener( object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                historyAdapter.filter.filter(newText)
                return false
            }

        })

        binding.rvNotiHistory.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        return view
    }


    val historyitemclicklistener = object : HistoryItemClickListener {
        override fun onHistoryItemClick(pos: Int) {
            var datalist : ArrayList<BalanceChanges> = ArrayList()
            if(tabposition == 0){
                datalist = (binding.rvNotiHistory.adapter as HistoryAdapter).getfiltereddata()
            }


            val type = datalist[pos].type
            when (type) {
                BalanceChanges.TYPE_TRANSFER -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToTransferResultFragment(
                            datalist[pos] as Transfer
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_TRANSFERFROM -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToTransferFromResultFragment(
                            datalist[pos] as TransferFrom
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_TOPUP -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToTopUpResultFragment(
                           datalist[pos] as TopUp
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_PAYMENT -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToPaymentResultFragment(
                            datalist[pos] as Payment
                        )
                    findNavController().navigate(action)
                }

                BalanceChanges.TYPE_WITHDRAW -> {
                    val action =
                        NotificationsFragmentDirections.actionNotificationsFragmentToWithDrawResuktFragment(
                            datalist[pos] as WithDraw
                        )
                    findNavController().navigate(action)
                }
            }
        }

        override fun onNotificationItemClick(pos: Int) {

        }
    }

    fun getnotificationdata(){
        notificationlist.clear()
        getnotificationtopup()
        getnotificationtransfer()
        getnotificationpayment()
        getnotificationtransferfrom()
        getnotificationwithdraw()
    }
    fun getnotificationtopup(){
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel

        historyNotificationViewModel.getallCurrentBalanceWithTopUp().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                for (currentBalanceWithTopUp in it){
                    Log.d(TAG, "onCreateViewCurrentBalanceWithTransfer: " + currentBalanceWithTopUp)
                    currentBalanceWithTopUp.currentBalanceWithBalanceChangesID.currentbalance?.let { it1 ->
                        currentBalanceWithTopUp.topUp?.let { it2 ->
                            BalanceChangesWithCurrentBalance(
                                it2,
                                it1
                            )
                        }
                    }?.let { it2 -> notificationlist.add(it2) }
                }
            }
        })

    }

    fun getnotificationtransfer(){
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel
        historyNotificationViewModel.getallCurrentBalanceWithTransfer().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "getnotificationtransfer: " + it)
            Log.d(TAG, "getnotificationtransfer: " + it.size)
            if (it != null) {
                for (currentBalanceWithTransfer in it){
                    Log.d(TAG, "onCreateViewCurrentBalanceWithTransfer: " + currentBalanceWithTransfer)
                    currentBalanceWithTransfer.currentBalanceWithBalanceChangesID.currentbalance?.let { it1 ->
                        currentBalanceWithTransfer.transfer?.let { it2 ->
                            BalanceChangesWithCurrentBalance(
                                it2,
                                it1
                            )
                        }
                    }?.let { it2 -> notificationlist.add(it2) }
                }
            }
        })
    }

    fun getnotificationpayment(){
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel

        historyNotificationViewModel.getallCurrentBalanceWithPayment().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                for (currentBalanceWithPayment in it){
                    currentBalanceWithPayment.currentBalanceWithBalanceChangesID.currentbalance?.let { it1 ->
                        currentBalanceWithPayment.payment?.let { it2 ->
                            BalanceChangesWithCurrentBalance(
                                it2,
                                it1
                            )
                        }
                    }?.let { it2 -> notificationlist.add(it2) }
                }
            }
        })

    }

    fun getnotificationtransferfrom(){
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel
        historyNotificationViewModel.getallCurrentBalanceWithTransferFrom().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                for (currentBalanceWithTransferFrom in it){
                    currentBalanceWithTransferFrom.currentBalanceWithBalanceChangesID.currentbalance?.let { it1 ->
                        currentBalanceWithTransferFrom.transferFrom?.let { it2 ->
                            BalanceChangesWithCurrentBalance(
                                it2,
                                it1
                            )
                        }
                    }?.let { it2 -> notificationlist.add(it2) }
                }
            }
        })
    }

    fun getnotificationwithdraw(){
        val historyNotificationViewModel = (activity as MainActivity).historyNotificationViewModel

        historyNotificationViewModel.getallCurrentBalanceWithWithDraw().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                for (currentBalanceWithWithDraw in it){
                    currentBalanceWithWithDraw.currentBalanceWithBalanceChangesID.currentbalance?.let { it1 ->
                        currentBalanceWithWithDraw.withDraw?.let { it2 ->
                            BalanceChangesWithCurrentBalance(
                                it2,
                                it1
                            )
                        }
                    }?.let { it2 -> notificationlist.add(it2) }
                }
            }
        })

    }
}