package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.Device
import com.example.model.Transfer
import com.example.model.TransferItem

@Database(entities = [Device::class, Transfer::class, TransferItem::class], version = 1, exportSchema = false)
abstract class ShareLoadDatabase : RoomDatabase() {
    abstract fun shareLoadDao(): ShareLoadDao
}
