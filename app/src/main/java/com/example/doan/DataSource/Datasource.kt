package com.example.doan.DataSource

import com.example.doan.R

class Datasource {
    fun loadNotifications(): List<noti> {
        return listOf<noti>(
            noti(
                R.drawable.moneytransfer,
                "giao dich",
                "chuyentien",
                "ban da chuyen tien cho 0935353535",
                "01-01-2023",
                "16:20"
            ),
            noti(
                R.drawable.moneytransfer,
                "giao dich",
                "chuyentien",
                "ban da chuyen tien cho 0935353535",
                "02-01-2023",
                "16:20"
            ),
            noti(
                R.drawable.moneytransfer,
                "giao dich",
                "chuyentien",
                "ban da chuyen tien cho 0935353535",
                "03-01-2023",
                "16:20"
            ),
            noti(
                R.drawable.moneytransfer,
                "giao dich",
                "chuyentien",
                "ban da chuyen tien cho 0935353535",
                "04-01-2023",
                "16:20"
            ),
            noti(
                R.drawable.moneytransfer,
                "giao dich",
                "chuyentien",
                "ban da chuyen tien cho 0935353535",
                "05-01-2023",
                "16:20"
            ),
        )


    }
}