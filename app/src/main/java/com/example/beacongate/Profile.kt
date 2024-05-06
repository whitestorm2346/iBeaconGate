package com.example.beacongate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.beacongate.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Returning the view")
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        database = FirebaseFirestore.getInstance()

        initTestData()

        binding.storeButton.setOnClickListener {
            val name = binding.entryName.text.toString()
            val gender = binding.entryGender.text.toString()
            val id = binding.entryId.text.toString().uppercase()
            val phone = binding.entryPhone.text.toString()
            val reason = binding.entryReason.text.toString()

            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val user = hashMapOf(
                "name" to name,
                "gender" to gender,
                "id" to id,
                "phone" to phone,
                "reason" to reason,
                "datetime" to formattedDateTime,
                "uuid" to null
            )
            Log.d(TAG, "User Data: $user")

            database.collection("Users").document(id).set(user)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "successfully stored the data", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed storing the data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun initTestData(){
        binding.entryName.setText("測試名")
        binding.entryGender.setText("男")
        binding.entryId.setText("A123456789")
        binding.entryPhone.setText("0123456789")
        binding.entryReason.setText("這個專案快失火了")
    }

    companion object {
        private const val TAG = "ProfileTest"
    }
}