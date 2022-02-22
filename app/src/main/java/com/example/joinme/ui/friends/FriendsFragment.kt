package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentFriendsBinding
import com.example.joinme.datastructure.Friends

class FriendsFragment : Fragment() {

    private val friendsViewModel: FriendsViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

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
        friendsViewModel.checkActivityState(friendDetailButton, friends, position, this)
    }

    fun onClickListener(friendDetailButton: TextView, friends: Array<Friends>, position: Int) {
        friendsViewModel.onClickListener( friendDetailButton, friends, position, this )
    }
}