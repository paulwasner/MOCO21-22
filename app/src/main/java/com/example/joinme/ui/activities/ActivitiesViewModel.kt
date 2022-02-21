package com.example.joinme.ui.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.joinme.R
import com.example.joinme.SharedViewModel
import com.example.joinme.datastructure.Activity
import com.example.joinme.datastructure.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ActivitiesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun checkLocationPermission(context: Context): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun requestLocationPermission(fragment: ActivitiesFragment) {
        val requestPermissionLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    MaterialAlertDialogBuilder(fragment.requireContext())
                        .setTitle("Standort-Zugriff")
                        .setMessage("Der Standort-Zugriff wurde erteilt.")
                        .setCancelable(true)
                        .show()
                } else {
                    MaterialAlertDialogBuilder(fragment.requireContext())
                        .setTitle("Standort-Zugriff")
                        .setMessage(
                            "Der Standort-Zugriff wurde verweigert.\r\n" +
                                    "Um eine Aktivität starten zu können, muss der " +
                                    "Standort-Zugriff erlaubt werden."
                        )
                        .setCancelable(true)
                        .show()
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun buttonClickListener(
        button: Button,
        activities: Array<Activity>,
        position: Int,
        fragment: ActivitiesFragment
    ) {
        val context = fragment.requireContext()
        val sharedViewModel = fragment.sharedViewModel
        val userRef = fragment.userRef

        var lastLocation: Location? = null
        val fusedLocationClient: FusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            lastLocation = location
        }

        //Update button color
        if (activities[position].started) {
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }

        button.setOnClickListener {
            if (!activities[position].started && !checkActivityStarted(activities)) {
                if (checkLocationPermission(context) && lastLocation != null) {
                    //Aktivität starten, wenn Permission gegeben ist
                    val locationString = "${lastLocation!!.latitude}, ${lastLocation!!.longitude}"
                    val user = sharedViewModel.user
                    val updatedUser = User(user.email, user.password, user.firstName, user.lastName,
                        locationString, true.toString(), activities[position].activityName,
                        user.friends)

                    //User in DB updaten
                    userRef.child(sharedViewModel.uuid).setValue(updatedUser)

                    //Button + Activity updaten
                    button.text = fragment.getString(R.string.sharing_stop)
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    activities[position].started = true

                    //Top-Status updaten
                    fragment.topInfo.text = activities[position].activityName

                    Toast.makeText(context,
                        "Standort: ${lastLocation!!.latitude}, ${lastLocation!!.longitude}",
                        Toast.LENGTH_SHORT).show()
                } else if (checkLocationPermission(context)) {
                    //Permisson granted aber kein Zugriff auf letzten Standort
                    Toast.makeText(context,
                        "Der letzte bekannte Standort kann nicht abgerufen werden!",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Standort-Freigabe nicht erteilt!",
                        Toast.LENGTH_SHORT).show()
                }
            } else if (activities[position].started) {
                //Aktivität beenden
                val user = sharedViewModel.user
                val updatedUser = User(user.email, user.password, user.firstName, user.lastName,
                    "", false.toString(), "", user.friends)

                //User in DB updaten
                userRef.child(sharedViewModel.uuid).setValue(updatedUser)

                //Button updaten
                button.text = fragment.getString(R.string.sharing_start)
                button.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                activities[position].started = false

                //Top-Status updaten
                fragment.topInfo.text = fragment.getString(R.string.no_activity_shared)
            } else {
                //Wenn bereits eine Aktivität gestartet wurde -> Toast
                Toast.makeText(context, "Es wurde bereits eine Aktivität gestartet",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkActivityStarted(activities: Array<Activity>): Boolean {
        activities.forEach {
            if (it.started)
                return true
        }
        return false
    }
}