package com.example.doan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.doan.Adapters.BankLinkingAdapter
import com.example.doan.ClicklistenerInterface.BanklistItemClickListener
import com.example.doan.DataSource.Banks
import com.example.doan.databinding.BanklistBinding


class BankListFragment : Fragment() {
    private var _binding: BanklistBinding? = null
    private val binding get() = _binding!!

    var dataset: java.util.ArrayList<Banks>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BanklistBinding.inflate(inflater, container, false)
        initData()

        binding.banklistrecyclerview.layoutManager = GridLayoutManager(this.context, 4)
        binding.banklistrecyclerview.adapter =
            BankLinkingAdapter(this, dataset, banklistItemClickListener)


        val view = binding.root

        return view
    }

    val banklistItemClickListener = object : BanklistItemClickListener {
        override fun onBanklistItemClick(pos: Int) {
            val bankitem = dataset?.get(pos)
            val action = bankitem?.let {
                BankListFragmentDirections.actionBankListFragmentToAddBankDetailFragment(
                    it
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }

    private fun initData() {
        dataset = ArrayList<Banks>()
        dataset!!.add(
            Banks(
                "https://infofinance.vn/wp-content/uploads/2020/11/bieu-tuong-cua-ngan-hang-bidv.jpg",
                "BIDV"
            )
        )
        dataset!!.add(
            Banks(
                "data:image/png;base64,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",
                "Viettinbank"
            )
        )
        dataset!!.add(
            Banks(
                "data:image/jpeg;base64,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",
                "Vietcombank"
            )
        )
        dataset!!.add(
            Banks(
                "https://cdn.haitrieu.com/wp-content/uploads/2022/01/Icon-Agribank.png",
                "Agribank"
            )
        )

    }
}