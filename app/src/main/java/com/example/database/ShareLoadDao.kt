package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.model.Device
import com.example.model.Transfer
import com.example.model.TransferItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShareLoadDao {
    @Query("SELECT * FROM devices ORDER BY lastConnected DESC")
    fun getAllDevices(): Flow<List<Device>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device)

    @Query("SELECT * FROM transfers ORDER BY timestamp DESC")
    fun getAllTransfers(): Flow<List<Transfer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransfer(transfer: Transfer)

    @Query("SELECT * FROM transfer_items WHERE transferId = :transferId")
    fun getTransferItems(transferId: String): Flow<List<TransferItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransferItem(item: TransferItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransferItems(items: List<TransferItem>)

    @Query("UPDATE transfer_items SET transferredBytes = :bytes, status = :status WHERE id = :id")
    suspend fun updateTransferItemProgress(id: String, bytes: Long, status: String)
    
    @Query("UPDATE transfers SET status = :status WHERE id = :id")
    suspend fun updateTransferStatus(id: String, status: String)
}
