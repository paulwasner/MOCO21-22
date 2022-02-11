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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentFriendsBinding
import com.example.joinme.datastructure.Friends
import com.example.joinme.datastructure.User
import com.example.joinme.ui.activities.ActivityListAdapter

class FriendsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val friendsViewModel: FriendsViewModel by viewModels()
    private lateinit var binding: FragmentFriendsBinding
    private var user = User("","", "", "", "","", "", mutableListOf())

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Angemeldeten User aus SharedViewModel holen
        user = sharedViewModel.user
        val friends =
            arrayOf(
                Friends(30, user.firstName!!),
                Friends(4, "Bob"),
                Friends(27, "Test")
            )

        val adapter = activity?.let { FriendsListAdapter(it, friends) }
        val listView: ListView = binding.root.findViewById(R.id.friends_listview)
        listView.adapter = adapter
    }
}