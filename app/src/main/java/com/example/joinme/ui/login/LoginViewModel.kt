package com.example.joinme.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.joinme.MainActivity
import com.example.joinme.databinding.ActivityLoginBinding
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LoginViewModel : ViewModel() {

    fun loginClickListener(binding: ActivityLoginBinding, activity: LoginActivity) {
        val context = activity.applicationContext
        val sharedViewModel = activity.sharedViewModel

        val emailTxt = binding.email.text.toString()
        val passwordTxt = binding.password.text.toString()

        //Toast, wenn der Benutzer keine Email oder kein Passwort eingegeben hat
        if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
            Toast.makeText(context, "Bitte Email und Passwort eingeben", Toast.LENGTH_SHORT)
                .show()
        } else {
            //Prüfen ob Nutzer (Email) existiert
            var uuid: String?
            sharedViewModel.emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(emailTxt)) {
                        uuid = snapshot.child(emailTxt).value as String?

                        //Wenn User existiert
                        if (uuid != null) {
                            sharedViewModel.userRef.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val getPassword = snapshot.child(uuid!!).child("password").value

                                    //Passwort-Überprüfung
                                    if (getPassword!! == passwordTxt) {
                                        Toast.makeText(context, "Login erfolgreich!",
                                            Toast.LENGTH_SHORT).show()
                                        //Aktuellen User holen
                                        val currentUser = User(
                                            snapshot.child(uuid!!).child("email").value as String?,
                                            snapshot.child(uuid!!).child("password").value as String?,
                                            snapshot.child(uuid!!).child("firstName").value as String?,
                                            snapshot.child(uuid!!).child("lastName").value as String?,
                                            snapshot.child(uuid!!).child("location").value as String?,
                                            snapshot.child(uuid!!).child("activityState").value as String?,
                                            snapshot.child(uuid!!).child("activityName").value as String?,
                                            snapshot.child(uuid!!).child("friends").value as MutableList<String>?
                                        )

                                        //Activities-Fragment öffnen
                                        Intent(context, MainActivity::class.java)
                                            .also {
                                                //User + UUID mit übergeben
                                                it.putExtra("currentUser", currentUser)
                                                it.putExtra("uuid", uuid)
                                                activity.startActivity(it)
                                            }
                                    } else {
                                        Toast.makeText(context, "Passwort falsch!",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        } else {
                            //Benutzer existiert nicht
                            Toast.makeText(context, "Email nicht registriert!",
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        //Benutzer existiert nicht
                        Toast.makeText(context, "Email nicht registriert!",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}