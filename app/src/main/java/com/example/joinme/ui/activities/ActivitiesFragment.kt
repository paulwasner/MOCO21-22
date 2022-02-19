package com.example.joinme.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.joinme.R
import com.example.joinme.databinding.FragmentActivitiesBinding
import com.example.joinme.datastructure.Activity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    ): View {
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
        val adapter = activity?.let { ActivityListAdapter(it, activities, this) }

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

    fun itemClickListener( view: View, activities: Array<Activity>, position: Int ) {
        val startActivityButton = view.findViewById<Button>(R.id.listtile_button)

        var lastLocation: Location? = null
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient( requireContext() )
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                lastLocation = location
            }

        startActivityButton.setOnClickListener {
            val permissionGranted = PackageManager.PERMISSION_GRANTED == ContextCompat
                .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)


            if (!activities[position].started && !checkActivityStarted(activities)) {
                //Aktivität starten, wenn Permission gegeben ist
                if (permissionGranted && lastLocation != null) {
                    startActivityButton.text = "Teilen beenden"
                    activities[position].started = true

                    Toast.makeText(
                        context,
                        "Standort: ${lastLocation!!.latitude}, ${lastLocation!!.longitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (permissionGranted) {
                    //Permisson granted aber kein Zugriff auf letzten Standort
                    Toast.makeText(
                        context,
                        "Der letzte bekannte Standort kann nicht abgerufen werden!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "Standort-Freigabe nicht erteilt!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (activities[position].started) {
                //Aktivität beenden
                startActivityButton.text = "Teilen starten"
                activities[position].started = false
            } else {
                //Wenn bereits eine Aktivität gestartet wurde -> Toast
                Toast.makeText(
                    context,
                    "Es wurde bereits eine Aktivität gestartet",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //TODO Button funktioniert erst beim zweiten Klick
            //TODO Standort in DB schreiben, bzw. löschen
            //TODO Top-Status aktuallisieren
            //TODO fixe Buttongröße
        }
    }

    //Prüfen, ob bereits eine Aktivität gestartet wurde
    private fun checkActivityStarted( activities: Array<Activity> ): Boolean {
        activities.forEach {
            if(it.started)
                return true
        }
        return false
    }
}