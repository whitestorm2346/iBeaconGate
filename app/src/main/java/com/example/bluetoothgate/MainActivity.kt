package com.example.bluetoothgate

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.bluetoothgate.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var qrCodeScanner: QRCodeScanner
    private lateinit var bluetoothFunctions: BluetoothFunctions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        qrCodeScanner = QRCodeScanner(this@MainActivity)
        bluetoothFunctions = BluetoothFunctions(this)
    }

    override fun onStart() {
        super.onStart()
        // 在 onStart 方法中啟動 Bluetooth 功能
        bluetoothFunctions.enableBluetooth()
    }

    override fun onResume() {
        super.onResume()
        // 在 onResume 方法中啟動 QR code scanner
        qrCodeScanner.startScanner()
    }

    override fun onPause() {
        super.onPause()
        // 在 onPause 方法中停止 QR code scanner
        qrCodeScanner.stopScanner()
    }

    override fun onStop() {
        super.onStop()
        // 在 onStop 方法中關閉 Bluetooth 功能
        bluetoothFunctions.disableBluetooth()
    }
}