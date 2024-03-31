package com.example.bluetoothgate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.integration.android.IntentIntegrator

class QRCodeScanner(private val activity: AppCompatActivity) {

    fun startScanner() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
            return
        }

        // 初始化 IntentIntegrator，並啟動 QR code scanner
        IntentIntegrator(activity).apply {
            setBeepEnabled(false)
            setCaptureActivity(ScanActivity::class.java)
            initiateScan()
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
    }
}
