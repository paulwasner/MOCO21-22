package com.example.joinme.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.joinme.R
import com.example.joinme.datastructure.Activity as ActivityData


class ActivityListAdapter(
    private val context: Activity,
    private val activities: Array<ActivityData>,
) :
    ArrayAdapter<ActivityData>(context, R.layout.activities_listtile, activities) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.activities_listtile, null, true)

        val activityName = rowView.findViewById<TextView>(R.id.listtile_title)
        val startActivityButton = rowView.findViewById<TextView>(R.id.listtile_button)
        startActivityButton.setOnClickListener {

            val permissionGranted = PackageManager.PERMISSION_GRANTED == ContextCompat
                        .checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION)


            if(!activities[position].started && !checkActivityStarted()){
                //Aktivität starten, wenn Permission gegeben ist
                    if( permissionGranted ) {
                        startActivityButton.text = "Teilen beenden"
                        activities[position].started = true
                    }
                else {
                    Toast.makeText(context, "Standort freigabe nicht erteilt!", Toast.LENGTH_SHORT).show()
                    }
            }
            else if(activities[position].started) {
                //Aktivität beenden
                startActivityButton.text = "Teilen starten"
                activities[position].started = false
            }
            else {
                //Wenn bereits eine Aktivität gestartet wurde -> Toast
                Toast.makeText(context, "Es wurde bereits eine Aktivität gestartet", Toast.LENGTH_SHORT).show()
            }
            //TODO Standort in DB schreiben, bzw. löschen
            //TODO Top-Status aktuallisieren
            //TODO fixe Buttongröße
        }

        val image = rowView.findViewById<ImageView>(R.id.listtile_image)
        image.clipToOutline = true

        activityName.text = activities[position].activityName

        return rowView
    }

    //Prüfen, ob bereits eine Aktivität gestartet wurde
    fun checkActivityStarted(): Boolean {
        activities.forEach {
            if(it.started)
                return true
        }
        return false
    }
}

