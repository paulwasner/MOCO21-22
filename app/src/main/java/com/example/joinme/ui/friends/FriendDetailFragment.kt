package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentFriendDetailBinding
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendDetailFragment : Fragment() {
    //Firebase
    private val database = FirebaseDatabase.getInstance(
        "https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/"
    )
    private val userRef = database.getReference("users")
    private val emailRef = database.getReference("emails")

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentFriendDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            //Aktuellen User + UUID holen
            val user = sharedViewModel.user

            val newFriend = binding.friendDetailEmail.text.toString()

            //Freundesliste holen
            val listOfFriends = user.friends
            val newFriendsList: MutableList<String> = mutableListOf()

            //Freunde der alten Liste in neue Liste schreiben
            listOfFriends?.forEach {
                newFriendsList.add(it)
            }
            //Prüfen, ob eingegebener Freund in DB existiert
            chekFriendsExistence( newFriend, newFriendsList, user )
        }
    }

    fun chekFriendsExistence(newFriend: String, newFriendsList: MutableList<String>, user: User ){
        //Existenz-Flag
        var existenceFlag = 0
        var friendId: String
        val uuid = sharedViewModel.uuid
        when {
            newFriend.isEmpty() -> {
                Toast.makeText(activity, "Bitte Feld ausfüllen!", Toast.LENGTH_SHORT).show()
            }
            newFriend == user.email -> {
                Toast.makeText(activity, "Benutzer kann nicht hinzugefügt werden!",
                    Toast.LENGTH_SHORT).show()
            }
            else -> {
                //Prüfen, ob Freund in DB exitiert
                emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
                                Toast.makeText(activity, "Benutzer bereits hinzugefügt",
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                //User aktuallisieren
                                val updatedUser = User( user.email, user.password,
                                    user.firstName, user.lastName, user.location,
                                    user.activityState, user.activityName, newFriendsList)
                                //User mit neuem Freund in DB speichen
                                userRef.child(uuid).setValue(updatedUser)
                                //User in SharedViewModel updaten
                                sharedViewModel.user = updatedUser
                                //ListOfFriends im sharedViewModel aktuallisieren
                                sharedViewModel.listOfFriends.add(Friends(friendId, newFriend))
                                Toast.makeText(activity, "Freund \"$newFriend\" hinzugefügt",
                                    Toast.LENGTH_SHORT).show()
                            }
                            //Aus Fragment heraus wechesln
                            activity?.onBackPressed()
                        } else {
                            //Freund existiert nicht
                            Toast.makeText(activity,"Freund exitiert nicht",
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
}