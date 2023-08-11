package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.doan.ClicklistenerInterface.MobileDataChangeBtnColor
import com.example.doan.DataSource.Data
import com.example.doan.DataSource.Mobile
import com.example.doan.FireBaseUserLiveData.ViewModel.ProjectViewModel
import com.example.doan.databinding.MobileDataTopupBinding
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class MobileDataFragment : Fragment() {

    private val viewModel by viewModels<ProjectViewModel>()
    private var _binding: MobileDataTopupBinding? = null
    private val binding get() = _binding!!
    private var mobile: Mobile = Mobile()
    private var data: Data = Data()
    override fun onResume() {
        super.onResume()
        binding.mobiledatatablayout.getTabAt(1)?.select()
        binding.mobiledatatablayout.getTabAt(0)?.select()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MobileDataTopupBinding.inflate(inflater, container, false)


        binding.mobiledatatablayout.addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                var fragment: Fragment? = null
                if (tab != null) {
                    when (tab.position) {
                        0 -> fragment = MobileFragment(mobileDataChangeBtnColor)

                        1 -> fragment = DataFragment(mobileDataChangeBtnColor)

                    }
                }
                var fm: FragmentManager? = activity?.supportFragmentManager
                var ft = fm?.beginTransaction()
                if (ft != null) {
                    if (fragment != null) {
                        ft.replace(R.id.fragmentsframlayout, fragment)
                    }
                }
                if (ft != null) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                }
                if (ft != null) {
                    ft.commit()
                }

            }


            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {

            }


            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {

            }

        })
        binding.mobiledatatoolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.btncontinue.setOnClickListener(View.OnClickListener {
            val phonenumber = binding.edtmobiledataphonenumber.text.toString()
            val carrier = binding.txtcarrername.text.toString()
            val action =
                MobileDataFragmentDirections.actionMobileDataFragmentToMobileDataPaymentFragment(
                    phonenumber,
                    carrier,
                    mobile,
                    data
                )
            findNavController().navigate(action)
        })
        val view = binding.root

        return view
    }

    val mobileDataChangeBtnColor = object : MobileDataChangeBtnColor {
        override fun onMobileDataChangeButtonColor() {
            context?.resources?.let { binding.btncontinue.setTextColor(it.getColor(R.color.white)) }
            context?.resources?.let { binding.btncontinue.setBackgroundColor(it.getColor(R.color.orange)) }
        }

        override fun onLoadMobileDatatoButton(mobile: Mobile) {
            this@MobileDataFragment.mobile = mobile
        }

        override fun onLoadMobileDatatoButton(data: Data) {
            this@MobileDataFragment.data = data
        }
    }
}