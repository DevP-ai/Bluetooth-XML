package com.developer.android.dev.technologia.androidapp.bluetooth_xml

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothReceiver:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
       val action = intent?.action
        if(action == BluetoothAdapter.ACTION_STATE_CHANGED){
            when(intent.action){

            }
        }
    }
}