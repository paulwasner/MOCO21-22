package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentFriendsBinding
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FriendsFragment : Fragment() {
    //Firebase
    val database = FirebaseDatabase.getInstance(
        "https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/")
    val userRef = database.getReference("users")
    val emailRef = database.getReference("emails")

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val friendsViewModel: FriendsViewModel by viewModels()

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private var user = User("","", "", "", "","", "", mutableListOf())

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //Freundesliste aus SharedViewModel holen + in Array<Friends> umwandeln
        val friends: Array<Friends> = sharedViewModel.listOfFriends.toTypedArray()

        //Anzeigen der Freundesliste
        val adapter = activity?.let { FriendsListAdapter(it, friends, this) }
        val listView: ListView = binding.root.findViewById(R.id.friends_listview)
        listView.adapter = adapter
    }

    fun checkActivityState(friendDetailButton: TextView, friends: Array<Friends>, position: Int) {
        //Aktivitätsstatus in DB überprüfen
        userRef.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val activityState = snapshot.child(friends[position].id).child("activityState").value as String
                if( activityState == true.toString() ) {
                    friendDetailButton.setBackgroundColor( ContextCompat.getColor( requireContext(), R.color.green ) )
                }
                else {
                    friendDetailButton.setBackgroundColor( ContextCompat.getColor( requireContext(), R.color.grey ) )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun onClickListener(friendDetailButton: TextView, friends: Array<Friends>, position: Int) {

        friendDetailButton.setOnClickListener {
            userRef.addListenerForSingleValueEvent( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val location = snapshot.child(friends[position].id).child("location").value as String
                    val activityName = snapshot.child(friends[position].id).child("activityName").value as String
                    val activityState = snapshot.child(friends[position].id).child("activityState").value

                    if( activityState == true.toString() ) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(friends[position].name)
                            .setMessage("Aktivität: $activityName\r\n" +
                                    "Standort: $location")
                            .setCancelable(true)
                            .show()
                    }
                    else {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(friends[position].name)
                            .setMessage("Keine Aktivität aktiv!")
                            .setCancelable(true)
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