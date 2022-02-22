package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
    val userRef = database.getReference("users")
    val emailRef = database.getReference("emails")

    private val friendsDetailViewModel: FriendDetailViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()

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
            friendsDetailViewModel.chekFriendsExistence(newFriend, newFriendsList, user, this)
        }
    }
}