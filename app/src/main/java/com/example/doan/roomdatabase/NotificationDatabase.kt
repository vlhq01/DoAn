package com.example.doan.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.doan.DataSource.*
import com.example.doan.utils.Constants

@Database(entities = [CurrentBalanceWithBalanceChangesID::class,TopUp::class,Transfer::class,TransferFrom::class,Payment::class,WithDraw::class], exportSchema = false, version = 1)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun getNotificationDao() : NotificationDao
    companion object{
        private var instance : NotificationDatabase?=null
        fun getInstance(context: Context) : NotificationDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context, NotificationDatabase::class.java,Constants.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return instance as NotificationDatabase
        }
    }
}