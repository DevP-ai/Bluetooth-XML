package com.developer.android.dev.technologia.androidapp.bluetooth_xml

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
    lateinit var discoverabilityReceiver: Discoverability

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
        discoverability()

        receiver = BluetoothReceiver()
        discoverabilityReceiver = Discoverability()

        binding.btnGetPairedDevices.setOnClickListener {
            getPairedDevices()
        }


    }

    private fun getPairedDevices() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED){

            try {
                var devices = bluetoothAdapter.bondedDevices
                for(device in devices){
                    Log.d("BTDevices","Device: ${device.name}, ${device.address},${device.uuids},${device.type}")
                }
            }catch (e:SecurityException){
                Log.d("BTError","SecurityException: ${e.message}")

            }
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH,android.Manifest.permission.BLUETOOTH_ADMIN),REQUEST_BLUETOOTH_PERMISSION)
        }
    }

    companion object{
        private const val REQUEST_BLUETOOTH_PERMISSION = 1
    }
    private fun discoverability() {
        when{
            ContextCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH_ADVERTISE
                ) == PackageManager.PERMISSION_GRANTED ->{

            }

            shouldShowRequestPermissionRationale(
                android.Manifest.permission.BLUETOOTH_ADVERTISE
            )->{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_ADVERTISE),101)
            }

        }


        binding.btnDiscoverbility.setOnClickListener {
            val discoverIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,20)
            startActivity(discoverIntent)

            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
            registerReceiver(discoverabilityReceiver,intentFilter)
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


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        unregisterReceiver(discoverabilityReceiver)
    }

}