package com.example.joinme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.joinme.R
import com.example.joinme.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password

        val loginButton = binding.loginButton
        val registerNowButton = binding.registerNowButton

        loginButton.setOnClickListener {
            val emailTxt = email.text.toString()
            val passwordTxt = password.text.toString()

            //Toast, wenn der Benutzer keine Email oder kein Passwort eingegeben hat
            if( emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText( applicationContext,
                    "Bitte Email und Passwort eingeben",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                //Daten an Firebase Realtime-DB senden
                //TODO
            }
        }

        //Falls noch kein Account existiert, zur Regestrierung weiterleiten
        registerNowButton.setOnClickListener {
            startActivity( Intent(applicationContext, RegistrationActivity::class.java) )
        }

    }
}