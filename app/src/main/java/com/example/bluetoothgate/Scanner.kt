package com.example.bluetoothgate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Scanner.newInstance] factory method to
 * create an instance of this fragment.
 */
class Scanner : Fragment() {

    private lateinit var codeScanner: CodeScanner;

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // 用户授予了权限
            // 执行其他操作...
        } else {
            // 用户拒绝了权限
            // 执行其他操作...
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_scanner, container, false)

        permissionCheck()

        val codeScannerView = rootView.findViewById<CodeScannerView>(R.id.zxing_barcode_scanner)

        codeScanner = CodeScanner(requireContext(), codeScannerView)
        codeScanner.setDecodeCallback {
            result -> val barcodeScanned = result.text

            requireActivity().runOnUiThread{
                handleScanResult(barcodeScanned)
            }
        }

        codeScannerView.setOnClickListener{
            codeScanner.startPreview()
        }

        return rootView
    }

    private fun handleScanResult(result: String) {

        Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()

        // 创建 Intent
        val intent = Intent(requireContext(), BLEBeaconAdvertise::class.java)
        intent.putExtra("qrCodeResult", result) // 将扫描结果作为参数传递
        startActivity(intent)

        // 停止相机预览（如果需要）
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


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.stopPreview()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Scanner.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Scanner().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}