package com.example.doan



import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.doan.AccountsandSettingFragment.Companion.TAG
import com.example.doan.Adapters.ViewpagerAdapter
import com.example.doan.Adapters.homeServiceAdapter
import com.example.doan.ClicklistenerInterface.HomeServiceItemClickListener
import com.example.doan.DataSource.LinkedBanks
import com.example.doan.DataSource.service
import com.example.doan.databinding.MainFragmentBinding
import com.firebase.ui.auth.IdpResponse

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    var adapter: homeServiceAdapter? = null
    var vpadapter: ViewpagerAdapter? = null
    var dataset: ArrayList<service>? = null
    var images: ArrayList<Int>? = null


    override fun onResume() {
        super.onResume()
        val mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.balance.observe(viewLifecycleOwner, Observer {
            binding.balance = it.toString() + "đ"
            Log.d(TAG, "onResume: " + it.toString())
            binding.upperLayout.txthiddenbalance.text = it.toString()
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        initData()
        adapter = dataset?.let { homeServiceAdapter(it, itemClickListener) }

        binding.bottomLayout.servicesRecyclerview.adapter = adapter

        binding.upperLayout.imgTopUp.setOnClickListener {
            val actiontopup = MainFragmentDirections.actionMainFragmentToMoneyInputFragment(
                LinkedBanks()
            )
            findNavController().navigate(actiontopup)
        }

        val bottomnavview = activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
        if (bottomnavview != null) {
            bottomnavview.visibility = View.VISIBLE
        }

        binding.upperLayout.imgTransfer.setOnClickListener {
            val actiontransfer = MainFragmentDirections.actionMainFragmentToContactsListFragment()
            findNavController().navigate(actiontransfer)
        }

        binding.upperLayout.imgWallet.setOnClickListener {
            val actionwallet = MainFragmentDirections.actionMainFragmentToWalletFragment()
            findNavController().navigate(actionwallet)
        }

        binding.upperLayout.imgWithDraw.setOnClickListener {
            val actionwithdraw =
                MainFragmentDirections.actionMainFragmentToWithdrawBankslistFragment()
            findNavController().navigate(actionwithdraw)
        }
        binding.toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.upperLayout.arrowButton.setOnClickListener { view: View? ->
            // If the CardView is already expanded, set its visibility
            // to gone and change the expand less icon to expand more.

            if (binding.upperLayout.hiddenView.visibility == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(
                    binding.upperLayout.baseCardview,
                    AutoTransition()
                )
                binding.upperLayout.hiddenView.visibility = View.GONE
                binding.upperLayout.arrowButton.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.upperLayout.baseCardview,
                    AutoTransition()
                )
                binding.upperLayout.hiddenView.visibility = View.VISIBLE
                binding.upperLayout.arrowButton.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }




        binding.appBarlayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    appBarLayout.viewTreeObserver.addOnGlobalLayoutListener {
                        if (binding.upperLayout.hiddenView.visibility == View.GONE) {
                            scrollRange = appBarLayout.totalScrollRange
                        }
                        if (binding.upperLayout.hiddenView.visibility == View.VISIBLE) {
                            scrollRange = appBarLayout.totalScrollRange
                        }
                    }
                }
                val range = (-scrollRange).toFloat()
                val offsetAlpha = appBarLayout.y / scrollRange
                val value = 255 - (255 * offsetAlpha * -1).toInt()
                binding.upperLayout.root.background.alpha =
                    (255 * (1 - verticalOffset / range)).toInt()
                //binding.upperLayout.root.background.alpha = value
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    (activity as AppCompatActivity).supportActionBar?.show()

                } else if (isShow) {
                    (activity as AppCompatActivity).supportActionBar
                        ?.hide() //careful there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })




        binding.bottomLayout.servicesRecyclerview.layoutManager = GridLayoutManager(context, 4)


        vpadapter = images?.let { ViewpagerAdapter(it) }
        binding.bottomLayout.bannerviewpager.adapter = vpadapter



        binding.bottomLayout.bannerviewpager.registerOnPageChangeCallback(object :
            OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                changeColor()
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }
        })
        val view = binding.root

        return view
    }

    fun changeColor() {
        when (binding.bottomLayout.bannerviewpager.currentItem) {
            0 -> {
                context?.resources?.let {
                    binding.bottomLayout.imgdot1.setBackgroundColor(
                        it.getColor(R.color.orange)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot2.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot3.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot4.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot5.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot6.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
            }
            1 -> {
                context?.resources?.let {
                    binding.bottomLayout.imgdot1.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot2.setBackgroundColor(
                        it.getColor(R.color.orange)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot3.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot4.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot5.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot6.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
            }
            2 -> {
                context?.resources?.let {
                    binding.bottomLayout.imgdot1.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot2.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot3.setBackgroundColor(
                        it.getColor(R.color.orange)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot4.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot5.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot6.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
            }
            3 -> {
                context?.resources?.let {
                    binding.bottomLayout.imgdot1.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot2.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot3.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot4.setBackgroundColor(
                        it.getColor(R.color.orange)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot5.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot6.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
            }
            4 -> {
                context?.resources?.let {
                    binding.bottomLayout.imgdot1.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot2.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot3.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot4.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot5.setBackgroundColor(
                        it.getColor(R.color.orange)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot6.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
            }
            5 -> {
                context?.resources?.let {
                    binding.bottomLayout.imgdot1.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot2.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot3.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot4.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot5.setBackgroundColor(
                        it.getColor(R.color.grey)
                    )
                }
                context?.resources?.let {
                    binding.bottomLayout.imgdot6.setBackgroundColor(
                        it.getColor(R.color.orange)
                    )
                }
            }
        }
    }

    private fun initData() {
        dataset = ArrayList<service>()
        dataset!!.add(service(R.drawable.shopeeicon, "Shopee"))
        dataset!!.add(service(R.drawable.shopeepayicon, "Shopeepay"))
        dataset!!.add(service(R.drawable.lazada, "lazada"))
        dataset!!.add(service(R.drawable.momoicon, "momo"))
        dataset!!.add(service(R.drawable.vnpayicon, "Vnpay"))
        dataset!!.add(service(R.drawable.zalopayicon, "Zalopay"))
        dataset!!.add(service(R.drawable.mobiletopu, "Mobile-TopUp"))
        dataset!!.add(service(R.drawable.movieicon, "Movies"))
        dataset!!.add(service(R.drawable.paybillicon, "Pay Bills"))
        dataset!!.add(service(R.drawable.insurranceicon, "Insurrances"))
        dataset!!.add(service(R.drawable.flighticon, "Flight"))
        dataset!!.add(service(R.drawable.datatopup, "Data"))
        dataset!!.add(service(R.drawable.ahamove, "ahamove"))
        dataset!!.add(service(R.drawable.shopeefoodicon, "Shopee Food"))
        dataset!!.add(service(R.drawable.hotelicon, "Hotels"))
        dataset!!.add(service(R.drawable.menuicon, "More"))
        images = ArrayList<Int>()
        images!!.add(R.drawable.shopeepaybanner1)
        images!!.add(R.drawable.shopeepaybanner2)
        images!!.add(R.drawable.shopeepaybanner3)
        images!!.add(R.drawable.shopeepaybanner4)
        images!!.add(R.drawable.shopeepaybanner5)
        images!!.add(R.drawable.shopeepaybanner6)
    }

    val itemClickListener = object : HomeServiceItemClickListener {
        override fun onItemClick(pos: Int) {
            if (dataset?.get(pos)?.serviceName == "Mobile-TopUp") {

                val mobileaction = MainFragmentDirections.actionMainFragmentToMobileDataFragment()
                findNavController().navigate(mobileaction)
                val bottomnavview =
                    activity?.findViewById<BottomNavigationView>(R.id.bottomnavigation)
                if (bottomnavview != null) {
                    bottomnavview.visibility = View.GONE
                }
            } else {
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO Listen to the result of the sign in process by filter for when
        //  SIGN_IN_REQUEST_CODE is passed back. Start by having log statements to know
        //  whether the user has signed in successfully
        if (requestCode == AccountsandSettingFragment.SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Log.i(
                    AccountsandSettingFragment.TAG,
                    "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
                val mainViewModel = (activity as MainActivity).mainViewModel
                mainViewModel.mainviewmodelobserve()
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(
                    AccountsandSettingFragment.TAG,
                    "Sign in unsuccessful ${response?.error?.errorCode}"
                )
            }
        }
    }

}