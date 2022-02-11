package com.example.joinme.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentFriendsBinding

class FriendDetailFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddFriend.setOnClickListener {
            //TODO Aktuellen User holen (SharedViewModel)
            //TODO Prüfen, ob eingegebener Freund in DB existiert
                //TODO Wenn ja, Freund zur Freundesliste hinzufügen (User aktuallisieren: SharedViewModel)
                    //TODO List of Friends in FriendsFragment aktuallisieren
                //TODO Wenn nein, Toast / Dialog + zum letzten Fragment zurückkenren
        }
    }
}