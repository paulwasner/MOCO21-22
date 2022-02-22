package com.example.joinme.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.joinme.MainActivity
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.ActivityLoginBinding
import com.example.joinme.datastructure.User
import com.example.joinme.ui.registration.RegistrationActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        val registerNowButton = binding.registerNowButton

        loginButton.setOnClickListener {
            loginViewModel.loginClickListener(binding, this)
        }

        //Falls noch kein Account existiert, zur Regestrierung weiterleiten
        registerNowButton.setOnClickListener {
            startActivity(Intent(applicationContext, RegistrationActivity::class.java))
        }

    }
}