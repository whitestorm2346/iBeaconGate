package com.example.beacongate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.example.beacongate.databinding.FragmentScannerBinding

class Scanner : Fragment() {
    private lateinit var binding: FragmentScannerBinding
    private lateinit var codeScanner: CodeScanner;

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "permission is granted")
        } else {
            Log.e(TAG, "permission is not granted")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannerBinding.inflate(inflater, container, false)

        permissionCheck()

        codeScanner = CodeScanner(requireContext(), binding.zxingBarcodeScanner)
        codeScanner.setDecodeCallback {
            result -> val barcodeScanned = result.text

            requireActivity().runOnUiThread{
                handleScanResult(barcodeScanned)
            }
        }

        binding.zxingBarcodeScanner.setOnClickListener{
            codeScanner.startPreview()
        }

        return binding.root
    }

    private fun handleScanResult(result: String) {
//        Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), BLEBeaconAdvertise::class.java)
        intent.putExtra("qrCodeResult", result) // 将扫描结果作为参数传递
        startActivity(intent)

        codeScanner.stopPreview()
    }


    private fun permissionCheck() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        codeScanner.stopPreview()
        codeScanner.releaseResources()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.stopPreview()
    }

    companion object {
        private const val TAG = "ScannerTest"
    }
}