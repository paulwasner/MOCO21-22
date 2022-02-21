package com.example.joinme

import androidx.lifecycle.ViewModel
import com.example.joinme.datastructure.Activity
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User

class SharedViewModel : ViewModel() {
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