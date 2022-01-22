package com.example.joinme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.joinme.MainActivity
import com.example.joinme.R
import com.example.joinme.databinding.ActivityLoginBinding
import com.example.joinme.datastructure.User
import com.example.joinme.ui.activities.ActivitiesFragment
import com.example.joinme.ui.activities.ActivitiesViewModel
import com.example.joinme.ui.activities.ActivityListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Firebase
    val database = FirebaseDatabase.getInstance(
        "https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/")
    val userRef = database.getReference("users")
    val emailRef = database.getReference("emails")

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
            if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Bitte Email und Passwort eingeben",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                //Prüfen ob Nutzer (Email) existiert
                var uuid: String?
                emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(emailTxt)) {
                            uuid = snapshot.child(emailTxt).value as String?

                            //Wenn User existiert
                            if (uuid != null) {
                                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val getPassword =
                                            snapshot.child(uuid!!).child("password").value

                                        //Passwort-Überprüfung
                                        if (getPassword!! == passwordTxt) {
                                            Toast.makeText(
                                                applicationContext, "Login erfolgreich!",
                                                Toast.LENGTH_SHORT
                                            ).show()
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
                                            val intent = Intent(
                                                applicationContext,
                                                MainActivity::class.java
                                            )
                                            //User mit übergeben
                                            intent.putExtra("currentUser", currentUser)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(
                                                applicationContext, "Passwort falsch!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            } else {
                                //Benutzer existiert nicht
                                Toast.makeText(
                                    applicationContext, "Email nicht registriert!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else {
                            //Benutzer existiert nicht
                            Toast.makeText(
                                applicationContext, "Email nicht registriert!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        //Falls noch kein Account existiert, zur Regestrierung weiterleiten
        registerNowButton.setOnClickListener {
            startActivity(Intent(applicationContext, RegistrationActivity::class.java))
        }

    }
}