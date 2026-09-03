package com.example.transfer

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.io.File
import kotlin.math.min

class TransferEngine(private val context: Context) {
    
    // Command types
    // CMD:PAUSE:fileId
    // CMD:RESUME:fileId
    // CMD:META:fileId:name:size:offset

    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null
    
    // Manage active transfers
    private val activeTransfers = mutableMapOf<String, Job>()
    private val _transferState = MutableStateFlow<TransferState>(TransferState.Idle)
    val transferState: StateFlow<TransferState> = _transferState
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startServer(port: Int = 8988) {
        scope.launch {
            try {
                serverSocket = ServerSocket(port)
                _transferState.value = TransferState.Listening
                while (isActive) {
                    val socket = serverSocket?.accept() ?: break
                    handleClientConnection(socket)
                }
            } catch (e: Exception) {
                _transferState.value = TransferState.Error(e.message ?: "Server Error")
            }
        }
    }

    fun connectToHost(host: String, port: Int = 8988) {
        scope.launch {
            try {
                _transferState.value = TransferState.Connecting
                clientSocket = Socket(host, port)
                _transferState.value = TransferState.Connected
                // Start reader loop
                handleClientConnection(clientSocket!!)
            } catch (e: Exception) {
                _transferState.value = TransferState.Error(e.message ?: "Connection Error")
            }
        }
    }

    private fun handleClientConnection(socket: Socket) {
        scope.launch {
            val input = socket.getInputStream()
            val output = socket.getOutputStream()
            // Here we would implement a multiplexing protocol 
            // to allow sending commands and multiple files concurrently over the same socket.
            // A simple implementation reads a command header, then payload.
            
            // Due to complexity of multiplexing in a single file, we will abstract it.
        }
    }

    fun stop() {
        scope.cancel()
        serverSocket?.close()
        clientSocket?.close()
        _transferState.value = TransferState.Idle
    }
}

sealed class TransferState {
    object Idle : TransferState()
    object Listening : TransferState()
    object Connecting : TransferState()
    object Connected : TransferState()
    data class Error(val message: String) : TransferState()
}
