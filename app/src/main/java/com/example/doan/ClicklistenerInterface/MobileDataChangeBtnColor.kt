package com.example.doan.ClicklistenerInterface

import com.example.doan.DataSource.Data
import com.example.doan.DataSource.Mobile

interface MobileDataChangeBtnColor {
    fun onMobileDataChangeButtonColor()
    fun onLoadMobileDatatoButton(mobile: Mobile)
    fun onLoadMobileDatatoButton(data: Data)
}