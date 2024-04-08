package com.example.bluetoothgate.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.bluetoothgate.MainActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class QRCodeScanner(private val activity: MainActivity) {
    private var isScanning = false

    fun startScanner() {
        if (isScanning) return

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

        isScanning = true
    }

    fun stopScanner() {
        if (!isScanning) return

        // 在这里执行停止扫描的操作
        // 例如，停止 ZXing 扫描器等

        isScanning = false
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
    }
}
