package com.example.joinme.ui.friends

import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.joinme.R
import com.example.joinme.datastructure.Friends
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FriendsViewModel : ViewModel() {
    fun checkActivityState(
        friendDetailButton: TextView,
        friends: Array<Friends>,
        position: Int,
        fragment: FriendsFragment
    ) {
        val sharedViewModel = fragment.sharedViewModel
        //Aktivitätsstatus in DB überprüfen
        sharedViewModel.userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activityState =
                    snapshot.child(friends[position].id).child("activityState").value as String
                if (activityState == true.toString()) {
                    friendDetailButton.setBackgroundColor(
                        ContextCompat.getColor(fragment.requireContext(), R.color.green)
                    )
                } else {
                    friendDetailButton.setBackgroundColor(
                        ContextCompat.getColor(fragment.requireContext(), R.color.grey)
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(fragment.context, "Fehler: $error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun onClickListener(
        friendDetailButton: TextView,
        friends: Array<Friends>,
        position: Int,
        fragment: FriendsFragment
    ) {
        val sharedViewModel = fragment.sharedViewModel
        friendDetailButton.setOnClickListener {
            sharedViewModel.userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val location =
                        snapshot.child(friends[position].id).child("location").value as String
                    val activityName =
                        snapshot.child(friends[position].id).child("activityName").value as String
                    val activityState =
                        snapshot.child(friends[position].id).child("activityState").value

                    if (activityState == true.toString()) {
                        MaterialAlertDialogBuilder(fragment.requireContext())
                            .setTitle(friends[position].name)
                            .setMessage("Aktivität: $activityName\r\nStandort: $location")
                            .setCancelable(true)
                            .show()
                    } else {
                        MaterialAlertDialogBuilder(fragment.requireContext())
                            .setTitle(friends[position].name)
                            .setMessage("Keine Aktivität aktiv!")
                            .setCancelable(true)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(fragment.context, "Fehler: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}