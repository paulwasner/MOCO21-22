package com.example.joinme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.joinme.R
import com.example.joinme.databinding.ActivityRegistrationBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    //Firebase Realtime-DB Referenz
    val database = Firebase.database.reference

    lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstname = binding.firstname
        val lastname = binding.lastname
        val email = binding.email
        val password = binding.password
        val passwordConf = binding.confirmPassword

        val registrationButton = binding.registrationButton
        val loginNowButton = binding.loginNowButton


        registrationButton.setOnClickListener {
            //Benutzerdaten einlesen
            val firstnameTxt = firstname.text.toString()
            val lastnameTxt = lastname.text.toString()
            val emailTxt = email.text.toString()
            val passwordTxt = password.text.toString()
            val passwordConfTxt = passwordConf.text.toString()

            //Prüfen, ob alle Felder ausgefüllt wurden
            if (firstnameTxt.isEmpty() ||
                lastnameTxt.isEmpty() ||
                emailTxt.isEmpty() ||
                passwordTxt.isEmpty() ||
                passwordConfTxt.isEmpty()
            ) {
                //Benutzer auf unausgefüllte Felder hinweisen
                Toast.makeText(
                    applicationContext,
                    "Bitte alle Felder ausfüllen",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            //Passwörter überprüfen
            else if (passwordTxt != passwordConfTxt) {
                Toast.makeText(
                    applicationContext,
                    "Passwörter stimmen nicht überein!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            //Wenn keine Fehler in der Eingabe vorleigen
            else {
                database.child("users").addListenerForSingleValueEvent( object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //Mail registriert?
                        if(snapshot.hasChild(emailTxt)) {
                            Toast.makeText(applicationContext, "Mail bereits registriert", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            database.child("users").child(emailTxt).child("first_name").setValue(firstnameTxt)
                            database.child("users").child(emailTxt).child("last_name").setValue(lastnameTxt)
                            database.child("users").child(emailTxt).child("email").setValue(emailTxt)

                            Toast.makeText( applicationContext, "Regestrierung erfolgreich", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                } )

                /*val uuid = UUID.randomUUID().toString()
                Log.d("UUID: ", uuid)*/
                //TODO UUID des Nutzers nach anmelden lokal speichern und an andere Activities / Fragments übergeben?



                /*
                //Eingaben an Datenbank senden
                database.child("users").child(uuid).child("email").setValue(emailTxt)
                database.child("users").child(uuid).child("first_name")
                    .setValue(firstnameTxt)
                database.child("users").child(uuid).child("last_name")
                    .setValue(lastnameTxt)
                database.child("users").child(uuid).child("password").setValue(password)*/



            }
        }
        //Falls Account bereits existiert, zum Login weiterleiten
        loginNowButton.setOnClickListener {
            finish()
        }
    }
}