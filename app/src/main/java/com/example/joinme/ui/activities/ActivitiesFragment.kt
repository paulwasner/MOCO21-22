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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.databinding.FragmentActivitiesBinding
import com.example.joinme.datastructure.Activity
import com.example.joinme.datastructure.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase


class ActivitiesFragment : Fragment() {

    //Firebase
    private val database = FirebaseDatabase.getInstance(
        "https://joinme-f75c5-default-rtdb.europe-west1.firebasedatabase.app/"
    )
    private val userRef = database.getReference("users")

    private lateinit var activitiesViewModel: ActivitiesViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!

    private lateinit var topInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
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
        var lastLocation: Location? = null
        val fusedLocationClient: FusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            lastLocation = location
        }

        //Update button color
        if (activities[position].started) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        button.setOnClickListener {
            if (!activities[position].started && !checkActivityStarted(activities)) {
                if (activitiesViewModel.checkLocationPermission(requireContext()) && lastLocation != null) {
                    //Aktivität starten, wenn Permission gegeben ist
                    val locationString = "${lastLocation!!.latitude}, ${lastLocation!!.longitude}"
                    val user = sharedViewModel.user
                    val updatedUser = User(user.email, user.password, user.firstName, user.lastName,
                        locationString, true.toString(), activities[position].activityName,
                        user.friends)

                    //User in DB updaten
                    userRef.child(sharedViewModel.uuid).setValue(updatedUser)

                    //Button + Activity updaten
                    button.text = getString(R.string.sharing_stop)
                    button.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green))
                    activities[position].started = true

                    //Top-Status updaten
                    topInfo.text = activities[position].activityName

                    Toast.makeText(context,
                        "Standort: ${lastLocation!!.latitude}, ${lastLocation!!.longitude}",
                        Toast.LENGTH_SHORT).show()
                } else if (activitiesViewModel.checkLocationPermission(requireContext())) {
                    //Permisson granted aber kein Zugriff auf letzten Standort
                    Toast.makeText(context,
                        "Der letzte bekannte Standort kann nicht abgerufen werden!",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,
                        "Standort-Freigabe nicht erteilt!", Toast.LENGTH_SHORT).show()
                }
            } else if (activities[position].started) {
                //Aktivität beenden
                val user = sharedViewModel.user
                val updatedUser = User(user.email, user.password, user.firstName, user.lastName,
                    "", false.toString(), "", user.friends)

                //User in DB updaten
                userRef.child(sharedViewModel.uuid).setValue(updatedUser)

                //Button updaten
                button.text = getString(R.string.sharing_start)
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                activities[position].started = false

                //Top-Status updaten
                topInfo.text = getString(R.string.no_activity_shared)
            } else {
                //Wenn bereits eine Aktivität gestartet wurde -> Toast
                Toast.makeText(context, "Es wurde bereits eine Aktivität gestartet",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Prüfen, ob bereits eine Aktivität gestartet wurde
    private fun checkActivityStarted(activities: Array<Activity>): Boolean {
        activities.forEach {
            if (it.started)
                return true
        }
        return false
    }
}