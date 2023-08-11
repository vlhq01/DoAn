package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doan.Adapters.DataAdapter
import com.example.doan.ClicklistenerInterface.DataItemClickListener
import com.example.doan.ClicklistenerInterface.MobileDataChangeBtnColor
import com.example.doan.ClicklistenerInterface.MobileItemClickListener

import com.example.doan.DataSource.Data
import com.example.doan.DataSource.Mobile

import com.example.doan.databinding.DataTopupFragmentBinding


class DataFragment(val mobileDataChangeBtnColor: MobileDataChangeBtnColor) : Fragment() {
    private var _binding: DataTopupFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataset: MutableList<Data>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataTopupFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        dataset = mutableListOf<Data>()
        dataset.add(Data("VinaPhone", 0.5, 7, 13000, 2))
        dataset.add(Data("VinaPhone", 1.0, 30, 20000, 3))
        dataset.add(Data("VinaPhone", 3.0, 30, 30000, 2))
        dataset.add(Data("VinaPhone", 5.0, 60, 50000, 3))
        binding.datarecyclerview.layoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.datarecyclerview.adapter =
            DataAdapter(dataset as ArrayList<Data>, dataItemClickListener)
        return view
    }

    val dataItemClickListener = object : DataItemClickListener {
        override fun onDataItemItemClick(pos: Int) {
            mobileDataChangeBtnColor.onMobileDataChangeButtonColor()
            mobileDataChangeBtnColor.onLoadMobileDatatoButton(dataset[pos])
        }

    }
}