package com.example.joinme

import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.joinme.datastructure.Friends
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainViewModel : ViewModel() {

    fun updateTopStatus(activity: MainActivity) {
        val sharedViewModel = activity.sharedViewModel

        if (sharedViewModel.user.activityState == true.toString()) {
            sharedViewModel.activityArray.forEach {
                if (it.activityName == sharedViewModel.user.activityName) {
                    it.started = true
                    //Top-Status updaten
                    val topStatus: TextView = activity.findViewById(R.id.top_status_info)
                    topStatus.text = it.activityName
                }
            }
        }
    }

    fun loadFriendsList(activity: MainActivity) {
        val sharedViewModel = activity.sharedViewModel
        val friendsId = sharedViewModel.user.friends
        friendsId?.forEach {
            sharedViewModel.userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child(it).child("firstName").value as String
                    val lastName = snapshot.child(it).child("lastName").value as String
                    val name = "$firstName $lastName"
                    val id = it
                    sharedViewModel.listOfFriends.add(Friends(id, name))
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}