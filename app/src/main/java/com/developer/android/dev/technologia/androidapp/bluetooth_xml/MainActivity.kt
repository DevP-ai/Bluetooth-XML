package com.developer.android.dev.technologia.androidapp.bluetooth_xml

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.developer.android.dev.technologia.androidapp.bluetooth_xml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var receiver :BluetoothReceiver

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        enableDisableBT()

        receiver = BluetoothReceiver()



        binding.btnDiscoverbility.setOnClickListener {

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun enableDisableBT() {
        when{
            ContextCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED ->{

                }

            shouldShowRequestPermissionRationale(
                android.Manifest.permission.BLUETOOTH_CONNECT
            )->{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT),101)
            }

        }
        binding.btnOnOff.setOnClickListener {
            if(!bluetoothAdapter.isEnabled){
                bluetoothAdapter.enable()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(intent)
                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                registerReceiver(receiver,intentFilter)
            }
            if(bluetoothAdapter.isEnabled){
                bluetoothAdapter.disable()

                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                registerReceiver(receiver,intentFilter)
            }
        }
    }

}