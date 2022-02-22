package com.example.joinme.ui.registration

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.ActivityRegistrationBinding
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class RegistrationViewModel : ViewModel() {
    fun registrationClickListener(
        binding: ActivityRegistrationBinding,
        activity: RegistrationActivity
    ) {
        val context = activity.applicationContext
        val sharedViewModel = activity.sharedViewModel

        //Benutzerdaten einlesen
        val firstnameTxt = binding.firstname.text.toString()
        val lastnameTxt = binding.lastname.text.toString()
        val emailTxt = binding.email.text.toString()
        val passwordTxt = binding.password.text.toString()
        val passwordConfTxt = binding.confirmPassword.text.toString()

        //Prüfen, ob alle Felder ausgefüllt wurden
        if (firstnameTxt.isEmpty() || lastnameTxt.isEmpty() || emailTxt.isEmpty() ||
            passwordTxt.isEmpty() || passwordConfTxt.isEmpty())
        {
            //Benutzer auf unausgefüllte Felder hinweisen
            Toast.makeText(context, "Bitte alle Felder ausfüllen",Toast.LENGTH_SHORT).show()
        }

        //Passwörter überprüfen
        else if (passwordTxt != passwordConfTxt) {
            Toast.makeText(context, "Passwörter stimmen nicht überein!",Toast.LENGTH_SHORT).show()
        }

        //Wenn keine Fehler in der Eingabe vorleigen
        else {
            //Neues User-Objekt erstellen
            val user = User(
                emailTxt, passwordTxt, firstnameTxt, lastnameTxt, "",
                "false", "", mutableListOf()
            )

            //Prüfen, ob Email registriert
            sharedViewModel.emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(user.email!!)) {
                        Toast.makeText(context, "Email ist bereits registriert!",
                            Toast.LENGTH_SHORT).show()
                    } else if (!snapshot.hasChild(user.email)) {
                        //UUID für neuen Nutzer generieren
                        val uuid = UUID.randomUUID().toString()
                        //User in DB einfügen
                        sharedViewModel.userRef.child(uuid).setValue(user)
                        //Zuweisung Email zu UUID für besseres Querrying
                        sharedViewModel.emailRef.child(emailTxt).setValue(uuid)
                        //Benutzer über Registrierung informieren
                        Toast.makeText(context, "User erfolgreich registriert",
                            Toast.LENGTH_SHORT).show()
                        activity.finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO
                }
            })
        }
    }
}