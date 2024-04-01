package com.example.bluetoothgate

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

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
            captureActivity = CaptureActivity::class.java
            initiateScan()
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
    }
}
