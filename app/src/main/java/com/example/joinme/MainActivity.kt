package com.example.joinme

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.joinme.databinding.ActivityMainBinding
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    //Firebase
    val database = FirebaseDatabase.getInstance(
        "https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/")
    val userRef = database.getReference("users")
    val emailRef = database.getReference("emails")

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val currentUser = intent.extras!!.get( "currentUser" ) as User
        val uuid = intent.extras!!.get( "uuid" ) as String
        sharedViewModel.user = currentUser
        sharedViewModel.uuid = uuid

        //ActivityList updaten
        if( sharedViewModel.user.activityState == true.toString() ) {
            sharedViewModel.activityArray.forEach {
                if( it.activityName == sharedViewModel.user.activityName ) {
                    it.started = true
                }
                Log.d("ACTIVITY0", "${it.activityName}, ${it.started}")
            }
        }



        //Freundesliste laden
        val friendsId = sharedViewModel.user.friends

        friendsId?.forEach {
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = snapshot.child(it).child("email").value as String
                    val id = it
                    sharedViewModel.listOfFriends.add(Friends(id, email))
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }
}