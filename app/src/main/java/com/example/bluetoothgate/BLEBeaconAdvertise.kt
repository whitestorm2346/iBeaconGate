package com.example.bluetoothgate

import android.Manifest
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bluetoothgate.databinding.ActivityBleBeaconAdvertiseBinding
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter


class BLEBeaconAdvertise : AppCompatActivity() {

    private lateinit var binding: ActivityBleBeaconAdvertiseBinding
    private lateinit var qrCodeResult: String

    private lateinit var beacon: Beacon
    private lateinit var beaconParser: BeaconParser
    private lateinit var beaconTransmitter: BeaconTransmitter

    private var isUuid = false

    private val advertisingCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            Log.d(Companion.TAG, "iBeacon Start Advertising")
            Toast.makeText(this@BLEBeaconAdvertise, "iBeacon start advertising", Toast.LENGTH_SHORT).show()

            binding.textView.setText(R.string.beacon_advertising)
            binding.imageView.setImageResource(R.drawable.baseline_bluetooth_audio_24)
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e(TAG, "Advertising onStartFailure: $errorCode")
            Toast.makeText(this@BLEBeaconAdvertise, "iBeacon on start failure: $errorCode", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBleBeaconAdvertiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            if(isUuid){
                if(beaconTransmitter.isStarted){
                    beaconTransmitter.stopAdvertising()
                    Log.d(Companion.TAG, "iBeacon Stop Advertising")

                    Toast.makeText(this, "iBeacon stop advertising", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        qrCodeResult = intent.getStringExtra("qrCodeResult").toString()
        isUuid = uuidRegex.matches(qrCodeResult)

        if(isUuid) {
            beacon = Beacon.Builder()
                .setId1(qrCodeResult)
                .setId2("0") // major
                .setId3("0") // minor
                .setManufacturer(0x004C)
                .setTxPower(-59)
                .build()
            Log.d(TAG, "Beacon: $beacon")

            beaconParser = BeaconParser()
                .setBeaconLayout(IBEACON_LAYOUT)
            Log.d(TAG, "Beacon Parser: $beaconParser")

            beaconTransmitter = BeaconTransmitter(this, beaconParser)
            beaconTransmitter.isConnectable = true
            Log.d(TAG, "Beacon Transmitter: $beaconTransmitter")


//            ### Use Android Beacon Library - AltBeacon
//
//            beacon = Beacon.Builder()
//                .setId1(qrCodeResult)
//                .setId2("0") // major
//                .setId3("0") // minor
//                .setManufacturer(0x0118)
//                .setTxPower(-59)
//                .build()
//            Log.d(TAG, "Beacon: $beacon")
//
//            beaconParser = BeaconParser()
//                .setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT)
//            Log.d(TAG, "Beacon Parser: $beaconParser")
//
//            beaconTransmitter = BeaconTransmitter(this, beaconParser)
//            beaconTransmitter.isConnectable = true
//            Log.d(TAG, "Beacon Transmitter: $beaconTransmitter")

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
                        123
                    )

                    beaconTransmitter.startAdvertising(beacon, advertisingCallback)
                }
                else {
                    Log.d(TAG, "Already Required Permission")

                    beaconTransmitter.startAdvertising(beacon, advertisingCallback)
                }
            }
        }
        else {
            binding.textView.setText(R.string.scanning_result_not_uuid)
            Toast.makeText(this, "The scanning result is not UUID", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(!beaconTransmitter.isStarted) {
            Log.d(Companion.TAG, "iBeacon Stop Advertising")
            beaconTransmitter.stopAdvertising()
        }
    }

    companion object {
        private const val TAG = "BeaconAdvertiseTest"
        private const val IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        private val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$")
    }
}