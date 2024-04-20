package com.example.bluetoothgate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bluetoothgate.bluetooth.BluetoothFunctions
import com.example.bluetoothgate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothFunctions: BluetoothFunctions

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Profile())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile -> replaceFragment(Profile())
                R.id.qr_code_scanner -> replaceFragment(Scanner())
                else -> {}
            }
            true
        }

        bluetoothFunctions = BluetoothFunctions(this)
    }

//    override fun onStart() {
//        super.onStart()
//        // 在 onStart 方法中啟動 Bluetooth 功能
//        bluetoothFunctions.enableBluetooth()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // 在 onResume 方法中啟動 QR code scanner
//        qrCodeScanner.startScanner()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // 在 onPause 方法中停止 QR code scanner
//        qrCodeScanner.stopScanner()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        // 在 onStop 方法中關閉 Bluetooth 功能
//        bluetoothFunctions.disableBluetooth()
//    }
}