package com.example.bluetoothgate

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bluetoothgate.databinding.ActivityBleBeaconAdvertiseBinding
import java.util.UUID

class BLEBeaconAdvertise : AppCompatActivity() {
    private val TAG = "BeaconAdvertise"
    private val REQUEST_BLUETOOTH_PERMISSION = 123
    private val advertisingCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
        }

        override fun onStartFailure(errorCode: Int) {
            Log.e(TAG, "Advertising onStartFailure: $errorCode")
            super.onStartFailure(errorCode)
        }
    }

    private lateinit var binding: ActivityBleBeaconAdvertiseBinding
    private lateinit var qrCodeResult: String

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeAdvertiser: BluetoothLeAdvertiser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBleBeaconAdvertiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            Log.d(TAG, "Stop Advertising")

            if (ActivityCompat.checkSelfPermission(
                    this,
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
            }
            bluetoothLeAdvertiser.stopAdvertising(advertisingCallback)

            Toast.makeText(this, "ble stop advertising", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        qrCodeResult = intent.getStringExtra("qrCodeResult").toString()

        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setConnectable(false) // 不允許連接
            .build()

        val pUuid = ParcelUuid(UUID.fromString(qrCodeResult))

        Log.d(TAG, "UUID: $pUuid")

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(true)
            .addServiceUuid(pUuid)
            .build()

        Log.d(TAG, "Data: $data")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d(TAG, "SDK Version Checking")

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Requesting Permission: Bluetooth Advertise")

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE),
                    REQUEST_BLUETOOTH_PERMISSION
                )

                bluetoothLeAdvertiser.startAdvertising(settings, data, advertisingCallback)

                Toast.makeText(this, "ble start advertising", Toast.LENGTH_SHORT).show()

                binding.textView.setText(R.string.beacon_advertising)
                binding.imageView.setImageResource(R.drawable.baseline_bluetooth_audio_24)
            }
            else {
                Log.d(TAG, "Already Required Permission")

                bluetoothLeAdvertiser.startAdvertising(settings, data, advertisingCallback)

                Toast.makeText(this, "ble start advertising", Toast.LENGTH_SHORT).show()

                binding.textView.setText(R.string.beacon_advertising)
                binding.imageView.setImageResource(R.drawable.baseline_bluetooth_audio_24)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (ActivityCompat.checkSelfPermission(
                this,
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

        Log.d(TAG, "Stop Advertising")

        bluetoothLeAdvertiser.stopAdvertising(advertisingCallback)

        Toast.makeText(this, "ble stop advertising", Toast.LENGTH_SHORT).show()
    }
}