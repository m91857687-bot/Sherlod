package com.example.model
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "devices")
data class Device(
    @PrimaryKey val id: String,
    val name: String,
    val lastConnected: Long,
    val totalSentBytes: Long = 0,
    val totalReceivedBytes: Long = 0
)
