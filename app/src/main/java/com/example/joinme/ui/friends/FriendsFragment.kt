package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.joinme.R
import com.example.joinme.databinding.FragmentFriendsBinding
import com.example.joinme.datastructure.Friends
import com.example.joinme.ui.activities.ActivityListAdapter

class FriendsFragment : Fragment() {

    private lateinit var friendsViewModel: FriendsViewModel
    private var _binding: FragmentFriendsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val friends =
            arrayOf(
                Friends(4, "Bob"),
                Friends(27, "Test")
            )

        val adapter = activity?.let { FriendsListAdapter(it, friends) }

        val listView: ListView = root.findViewById(R.id.friends_listview)
        listView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}