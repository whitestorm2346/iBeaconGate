package com.example.bluetoothgate

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.AdvertiseSettings.ADVERTISE_MODE_LOW_POWER
import android.bluetooth.le.AdvertiseSettings.ADVERTISE_TX_POWER_LOW
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import java.util.UUID

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
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }

    // 啟用藍牙
    fun enableBluetooth() {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

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
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

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

            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            // 將發現時間設置為 0 秒，即表示設備將不再可見
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0)
            context.startActivity(discoverableIntent)
        }
    }

    fun stringToBluetoothSignal(data: String): ByteArray {
        // 將字串轉換成 byte array
        return data.toByteArray(Charsets.UTF_8)
    }

    // 廣播藍牙訊號
    fun broadcastBluetoothSignal(signalData: ByteArray) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            // 藍牙未啟用，或設備不支援藍牙
            return
        }

        val bluetoothLeAdvertiser: BluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser
            ?: // 不支援 BLE 廣播
            return

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(ADVERTISE_MODE_LOW_POWER)
            .setTxPowerLevel(ADVERTISE_TX_POWER_LOW)
            .setConnectable(false)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addServiceUuid(ParcelUuid(UUID.randomUUID()))
            .addServiceData(ParcelUuid(UUID.randomUUID()), signalData)
            .build()

        val advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                super.onStartSuccess(settingsInEffect)
                // 廣播成功開始
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                // 廣播開始失敗
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADVERTISE
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

        bluetoothLeAdvertiser.startAdvertising(settings, data, advertiseCallback)
    }

    // 其他藍牙相關功能可在此添加
}
