package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentFriendsBinding
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User


class FriendsFragment : Fragment() {

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
        val adapter = activity?.let { FriendsListAdapter(it, friends) }
        val listView: ListView = binding.root.findViewById(R.id.friends_listview)
        listView.adapter = adapter
    }
}