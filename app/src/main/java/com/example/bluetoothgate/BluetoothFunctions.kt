package com.example.bluetoothgate

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class BluetoothFunctions(private val context: Context) {

    // 檢查是否支援藍牙
    fun isBluetoothSupported(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    }

    // 初始化藍牙權限
    fun initializeBluetoothPermissions() {
        // 在 Android 6.0 及以上版本中，需要在運行時檢查並申請藍牙相關權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // TODO: 在這裡檢查和申請藍牙相關權限
        }
    }

    // 檢查藍牙是否已啟用
    fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }

    // 啟用藍牙
    fun enableBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            context.startActivity(enableBluetoothIntent)
        }
    }

    // 關閉藍牙
    fun disableBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            bluetoothAdapter.disable()
        }
    }

    // 廣播藍牙訊號
    fun broadcastBluetoothSignal(signalData: ByteArray) {
        // TODO: 實現廣播藍牙訊號的功能
    }

    // 其他藍牙相關功能可在此添加
}
