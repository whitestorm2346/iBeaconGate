package com.example.bluetoothgate

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.bluetoothgate.databinding.ActivityMainBinding
import com.example.bluetoothgate.BluetoothFunctions

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainTitle.text = "change in main activity"
    }
}