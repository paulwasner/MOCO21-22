package com.example.joinme.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.joinme.R
import com.example.joinme.databinding.FragmentActivitiesBinding
import com.example.joinme.datastructure.Activity
import com.example.joinme.datastructure.Participants

class ActivitiesFragment : Fragment() {

    private lateinit var activitiesViewModel: ActivitiesViewModel
    private var _binding: FragmentActivitiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activitiesViewModel =
            ViewModelProvider(this).get(ActivitiesViewModel::class.java)

        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activities =
            arrayOf(
                Activity("Schwimmen gehen", Participants(1, 4), false),
                Activity("Bowlen", Participants(1, 2), false),
                Activity("Kinobesuch", Participants(1, 2), false),
                Activity("Park", Participants(1, 2), false),
                Activity("Grillen", Participants(1, 2), false),
                Activity("Fitnessstudio", Participants(1, 2), false),
                Activity("Programmieren", Participants(1, 2), false),
                Activity("Lernen", Participants(1, 2), false),
                Activity("Testen", Participants(1, 2), false)
            )
        val adapter = activity?.let { ActivityListAdapter(it, activities) }

        val listView: ListView = root.findViewById(R.id.activity_listview)
        listView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}