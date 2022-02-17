package com.example.joinme.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentActivitiesBinding
import com.example.joinme.datastructure.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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
                Activity("Schwimmen gehen", false),
                Activity("Bowlen", false),
                Activity("Kinobesuch", false),
                Activity("Park", false),
                Activity("Grillen", false),
                Activity("Fitnessstudio", false),
                Activity("Programmieren", false),
                Activity("Lernen", false)
            )
        val adapter = activity?.let { ActivityListAdapter(it, activities) }

        val listView: ListView = root.findViewById(R.id.activity_listview)
        listView.adapter = adapter

        if(!checkLocationPermission()) {
            requestLocationPermission()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkLocationPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission() )
        { isGranted: Boolean ->
            if (isGranted) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Standort-Zugriff")
                    .setMessage("Der Standort-Zugriff wurde erteilt.")
                    .setCancelable(true)
                    .show()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Standort-Zugriff")
                    .setMessage("Der Standort-Zugriff wurde verweigert.\r\n" +
                            "Um eine Aktivität starten zu können, muss der " +
                            "Standort-Zugriff erlaubt werden.")
                    .setCancelable(true)
                    .show()
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}