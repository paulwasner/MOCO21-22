package com.example.joinme.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentActivitiesBinding
import com.example.joinme.datastructure.Activity

class ActivitiesFragment : Fragment() {

    private val activitiesViewModel: ActivitiesViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!

    lateinit var topInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = activity?.let { ActivityListAdapter(it, sharedViewModel.activityArray, this) }

        val listView: ListView = view.findViewById(R.id.activity_listview)
        listView.adapter = adapter

        topInfo = view.findViewById(R.id.top_status_info)

       if (!activitiesViewModel.checkLocationPermission(requireContext())) {
            activitiesViewModel.requestLocationPermission(this)
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateButtonOnStartUp(button: Button, activities: Array<Activity>, position: Int) {
        if (activities[position].started) {
            button.text = getString(R.string.sharing_stop)
        }
    }

    fun itemClickListener(button: Button, activities: Array<Activity>, position: Int) {
        activitiesViewModel.buttonClickListener( button, activities, position, this )
    }
}