package com.example.joinme.ui.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ActivitiesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun checkLocationPermission( context: Context ): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun requestLocationPermission( fragment: ActivitiesFragment ) {
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
}