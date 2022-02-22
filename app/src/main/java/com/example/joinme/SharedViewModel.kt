package com.example.joinme

import androidx.lifecycle.ViewModel
import com.example.joinme.datastructure.Activity
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User
import com.google.firebase.database.FirebaseDatabase

class SharedViewModel : ViewModel() {
    //Firebase
    private val database = FirebaseDatabase.getInstance(
        "https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/"
    )
    val userRef = database.getReference("users")
    val emailRef = database.getReference("emails")

    var user = User("", "", "", "", "", "", "", mutableListOf())
    var uuid = ""
    var listOfFriends: MutableList<Friends> = mutableListOf()

    var activityArray = arrayOf(
        Activity("Schwimmen gehen", false),
        Activity("Bowlen", false),
        Activity("Kinobesuch", false),
        Activity("Park", false),
        Activity("Grillen", false),
        Activity("Fitnessstudio", false),
        Activity("Programmieren", false),
        Activity("Lernen", false)
    )
}