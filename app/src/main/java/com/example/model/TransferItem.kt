package com.example.model
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "transfer_items")
data class TransferItem(
    @PrimaryKey val id: String,
    val transferId: String,
    val name: String,
    val size: Long,
    val uri: String,
    val type: String,
    val transferredBytes: Long,
    val status: String
)
