package com.example.model
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "transfers")
data class Transfer(
    @PrimaryKey val id: String,
    val deviceId: String,
    val isSender: Boolean,
    val totalFiles: Int,
    val totalBytes: Long,
    val timestamp: Long,
    val status: String
)
