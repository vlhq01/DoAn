package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.doan.Adapters.MobileAdapter
import com.example.doan.ClicklistenerInterface.MobileDataChangeBtnColor
import com.example.doan.ClicklistenerInterface.MobileItemClickListener

import com.example.doan.DataSource.Mobile

import com.example.doan.databinding.MobileTopupFragmentBinding

class MobileFragment(val mobileDataChangeBtnColor: MobileDataChangeBtnColor) : Fragment() {
    private var _binding: MobileTopupFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataset: MutableList<Mobile>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MobileTopupFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        dataset = mutableListOf<Mobile>()
        dataset.add(Mobile(10000, 6))
        dataset.add(Mobile(20000, 4))
        dataset.add(Mobile(30000, 6))
        dataset.add(Mobile(50000, 1))
        dataset.add(Mobile(100000, 6))
        dataset.add(Mobile(200000, 4))
        dataset.add(Mobile(300000, 6))
        dataset.add(Mobile(500000, 1))
        binding.mobilerecyclerview.layoutManager = GridLayoutManager(
            this.context,
            3, RecyclerView.VERTICAL, false
        )
        binding.mobilerecyclerview.adapter =
            MobileAdapter(dataset as ArrayList<Mobile>, mobileItemClickListener)

        return view
    }

    val mobileItemClickListener = object : MobileItemClickListener {
        override fun onMobileItemItemClick(pos: Int) {
            mobileDataChangeBtnColor.onMobileDataChangeButtonColor()
            mobileDataChangeBtnColor.onLoadMobileDatatoButton(dataset[pos])
        }
    }
}