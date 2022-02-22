package com.example.joinme.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.ActivityRegistrationBinding
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding
    private val registrationViewModel: RegistrationViewModel by viewModels()
    val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registrationButton = binding.registrationButton
        val loginNowButton = binding.loginNowButton


        registrationButton.setOnClickListener {
            registrationViewModel.registrationClickListener(binding, this)
        }

        //Falls Account bereits existiert, zum Login weiterleiten
        loginNowButton.setOnClickListener {
            finish()
        }
    }
}