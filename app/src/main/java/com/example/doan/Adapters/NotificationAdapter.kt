package com.example.doan.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.doan.ClicklistenerInterface.HistoryItemClickListener
import com.example.doan.DataSource.*
import com.example.doan.MainActivity
import com.example.doan.ViewModels.MainViewModel
import com.example.doan.databinding.NotificationitemBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class NotificationAdapter(
    private var mListData: ArrayList<BalanceChangesWithCurrentBalance>,
    var mcallback: HistoryItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null

    inner class NotificationViewHolder(val binding: NotificationitemBinding, val viewType: Int) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(item: BalanceChangesWithCurrentBalance) {
            when (viewType) {
                BalanceChanges.TYPE_TRANSFER -> (item).let {
                    binding.txtnotificationtitle.text = "Money Transfer Completed"
                    val blc = item.balanceChanges as Transfer
                    binding.txtNotificationcontent.text =
                        " You have transferred đ" + blc.amount + " to " + blc.receivername + ". Transaction No: " + blc.odersn + ". Your current balance is " + item.currentbalance.toInt()
                            .toString() + "đ"
                    binding.txtnotificationdate.text = blc.date
                }

                BalanceChanges.TYPE_TOPUP -> (item).let {
                    binding.txtnotificationtitle.text = "Money TopUp Completed"
                    val blc = item.balanceChanges as TopUp
                    binding.txtNotificationcontent.text =
                        " Your To-up request request " + blc.odersn + " is successful and " + blc.amount + "đ has been credited to your account. Your current account balance is " + item.currentbalance.toInt()
                            .toString() + "đ"
                    binding.txtnotificationdate.text = blc.date
                }

                BalanceChanges.TYPE_PAYMENT -> (item).let {
                    binding.txtnotificationtitle.text = "Payment Completed"
                    val blc = item.balanceChanges as Payment
                    binding.txtNotificationcontent.text =
                        " You have scucessfully made  a Payment with value of " + blc.amount + "đ and " + "Transaction No: " + blc.odersn + ". Your current balance is " + item.currentbalance.toInt()
                            .toString() + "đ"
                    binding.txtnotificationdate.text = blc.date
                }

                BalanceChanges.TYPE_TRANSFERFROM -> (item).let {
                    binding.txtnotificationtitle.text = "Money Received"
                    val blc = item.balanceChanges as TransferFrom

                    binding.txtNotificationcontent.text =
                        " Your have received " + blc.amount + "đ from " + blc.sendername + ". Transaction No: " + blc.odersn + ". Your current account balance is " + item.currentbalance.toInt()
                            .toString() + "đ"

                    binding.txtnotificationdate.text = blc.date
                }

                BalanceChanges.TYPE_WITHDRAW -> (item).let {
                    binding.txtnotificationtitle.text = "Money Withdraw Completed"

                    val blc = item.balanceChanges as WithDraw
                    binding.txtNotificationcontent.text =
                        " Your have withdraw " + blc.amount + "đ to bank account with name" + blc.bank + " and number" + blc.banknumber + ". Transaction No: " + blc.odersn + ". Your current account balance is " + item.currentbalance.toInt()
                            .toString() + "đ"

                    binding.txtnotificationdate.text = blc.date
                }
            }
        }

        init {
            binding.root.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            mcallback.onHistoryItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return NotificationViewHolder(
            NotificationitemBinding.inflate(
                layoutInflater
            ), viewType
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NotificationAdapter.NotificationViewHolder).bind(mListData[position])
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun getItemViewType(position: Int): Int {
        return mListData[position].balanceChanges.type
    }
}