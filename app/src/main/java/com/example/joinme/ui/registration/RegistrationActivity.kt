package com.example.joinme.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.joinme.R
import com.example.joinme.databinding.ActivityRegistrationBinding
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding

    //Firebase
    val database = FirebaseDatabase.getInstance("https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/")
    val userRef = database.getReference("users")
    val emailRef = database.getReference("emails")

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

            /*Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()
            //Test DB-Write
            dbRef.setValue("Test")*/

            //Benutzerdaten einlesen
            val firstnameTxt = firstname.text.toString()
            val lastnameTxt = lastname.text.toString()
            val emailTxt = email.text.toString()
            val passwordTxt = password.text.toString()
            val passwordConfTxt = passwordConf.text.toString()

            //Prüfen, ob alle Felder ausgefüllt wurden
            if (firstnameTxt.isEmpty() || lastnameTxt.isEmpty() || emailTxt.isEmpty() ||
                passwordTxt.isEmpty() || passwordConfTxt.isEmpty() ) {

                //Benutzer auf unausgefüllte Felder hinweisen
                Toast.makeText( applicationContext, "Bitte alle Felder ausfüllen",
                    Toast.LENGTH_SHORT ).show()
            }

            //Passwörter überprüfen
            else if (passwordTxt != passwordConfTxt) {
                Toast.makeText( applicationContext, "Passwörter stimmen nicht überein!",
                    Toast.LENGTH_SHORT ).show()
            }

            //Wenn keine Fehler in der Eingabe vorleigen
            else {
                //Neues User-Objekt erstellen
                val user = User( emailTxt, passwordTxt, firstnameTxt, lastnameTxt, "",
                    "", "", mutableListOf() )

                //Prüfen, ob Email registriert
                emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.hasChild(user.email!!)) {
                            Toast.makeText(applicationContext, "Email ist bereits registriert!",
                                Toast.LENGTH_SHORT).show()
                        }
                        else if ( !snapshot.hasChild(user.email) ){
                            //UUID für neuen Nutzer generieren
                            val uuid = UUID.randomUUID().toString()
                            //User in DB einfügen
                            userRef.child(uuid).setValue(user)

                            //Zuweisung Email zu UUID für besseres Querrying
                            emailRef.child(emailTxt).setValue(uuid)

                            //Benutzer über Registrierung informieren
                            Toast.makeText(applicationContext, "User erfolgreich registriert",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //TODO
                    }
                })
            }
        }
        //Falls Account bereits existiert, zum Login weiterleiten
        loginNowButton.setOnClickListener {
            finish()
        }
    }
}