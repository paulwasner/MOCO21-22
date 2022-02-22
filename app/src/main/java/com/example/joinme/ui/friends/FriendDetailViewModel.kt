package com.example.joinme.ui.friends

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FriendDetailViewModel : ViewModel() {
    fun chekFriendsExistence(
        newFriend: String,
        newFriendsList: MutableList<String>,
        user: User,
        fragment: FriendDetailFragment
    ) {
        val sharedViewModel = fragment.sharedViewModel
        val context = fragment.context

        //Existenz-Flag -> Merkt sich, ob Freund bereits hinzugefügt wurde
        var existenceFlag = 0
        var friendId: String
        val uuid = sharedViewModel.uuid
        when {
            newFriend.isEmpty() -> {
                Toast.makeText(context, "Bitte Feld ausfüllen!", Toast.LENGTH_SHORT).show()
            }
            newFriend == user.email -> {
                Toast.makeText(context, "Benutzer kann nicht hinzugefügt werden!",
                    Toast.LENGTH_SHORT).show()
            }
            else -> {
                //Prüfen, ob Freund in DB exitiert
                fragment.emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(newFriend)) {
                            friendId = snapshot.child(newFriend).value as String

                            //Prüfen, ob Freund bereits in der Liste
                            if (!newFriendsList.contains(friendId)) {
                                //Neuen Freund hinzufügen
                                newFriendsList.add(friendId)
                            } else {
                                existenceFlag = 1
                            }

                            //Wenn Freund bereits in der Liste
                            if (existenceFlag == 1) {
                                Toast.makeText(context, "Benutzer bereits hinzugefügt",
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                //User aktuallisieren
                                val updatedUser = User(user.email, user.password,
                                    user.firstName, user.lastName, user.location,
                                    user.activityState, user.activityName, newFriendsList)
                                //User mit neuem Freund in DB speichen
                                fragment.userRef.child(uuid).setValue(updatedUser)
                                //User in SharedViewModel updaten
                                sharedViewModel.user = updatedUser
                                //ListOfFriends im sharedViewModel aktuallisieren
                                sharedViewModel.listOfFriends.add(Friends(friendId, newFriend))
                                Toast.makeText(context, "Freund \"$newFriend\" hinzugefügt",
                                    Toast.LENGTH_SHORT).show()
                            }
                            //Aus Fragment heraus wechesln
                            fragment.activity?.onBackPressed()
                        } else {
                            //Freund existiert nicht
                            Toast.makeText(context, "Freund exitiert nicht",Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }
}