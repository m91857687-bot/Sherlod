package com.example.repository

import com.example.database.ShareLoadDao
import com.example.model.Device
import com.example.model.Transfer
import com.example.model.TransferItem
import kotlinx.coroutines.flow.Flow

class ShareLoadRepository(private val dao: ShareLoadDao) {
    val allDevices: Flow<List<Device>> = dao.getAllDevices()
    val allTransfers: Flow<List<Transfer>> = dao.getAllTransfers()

    suspend fun insertDevice(device: Device) = dao.insertDevice(device)
    
    suspend fun insertTransfer(transfer: Transfer) = dao.insertTransfer(transfer)
    
    fun getTransferItems(transferId: String): Flow<List<TransferItem>> = dao.getTransferItems(transferId)
    
    suspend fun insertTransferItem(item: TransferItem) = dao.insertTransferItem(item)
    
    suspend fun insertTransferItems(items: List<TransferItem>) = dao.insertTransferItems(items)
    
    suspend fun updateTransferItemProgress(id: String, bytes: Long, status: String) {
        dao.updateTransferItemProgress(id, bytes, status)
    }
    
    suspend fun updateTransferStatus(id: String, status: String) {
        dao.updateTransferStatus(id, status)
    }
}
