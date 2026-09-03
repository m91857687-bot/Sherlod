package com.example.discovery

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiP2pManagerWrapper(private val context: Context) {
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }
    private var wifiChannel: WifiP2pManager.Channel? = null

    init {
        wifiChannel = manager?.initialize(context, Looper.getMainLooper(), null)
    }

    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    @SuppressLint("MissingPermission")
    fun discoverPeers(): Flow<List<WifiP2pDevice>> = callbackFlow {
        val p2pChannel = wifiChannel
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION == intent.action) {
                    manager?.requestPeers(p2pChannel) { peers: WifiP2pDeviceList? ->
                        trySend(peers?.deviceList?.toList() ?: emptyList())
                    }
                }
            }
        }
        context.registerReceiver(receiver, intentFilter)
        manager?.discoverPeers(p2pChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {}
            override fun onFailure(reasonCode: Int) {
                trySend(emptyList())
            }
        })
        awaitClose {
            context.unregisterReceiver(receiver)
            manager?.stopPeerDiscovery(p2pChannel, null)
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToPeer(device: WifiP2pDevice): Flow<WifiP2pInfo?> = callbackFlow {
        val p2pChannel = wifiChannel
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
        }
        
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION == intent.action) {
                    manager?.requestConnectionInfo(p2pChannel) { info ->
                        trySend(info)
                    }
                }
            }
        }
        context.registerReceiver(receiver, intentFilter)

        manager?.connect(p2pChannel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {}
            override fun onFailure(reason: Int) {
                trySend(null)
            }
        })
        
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
    
    fun disconnect() {
        manager?.removeGroup(wifiChannel, null)
    }
}
