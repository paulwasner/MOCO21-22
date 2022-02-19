package com.example.joinme.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.joinme.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.joinme.datastructure.Activity as ActivityData


class ActivityListAdapter(
    private val context: Activity,
    private val activities: Array<ActivityData>,
    private val fragment: ActivitiesFragment
) :
    ArrayAdapter<ActivityData>(context, R.layout.activities_listtile, activities) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.activities_listtile, null, true)

        val activityName = rowView.findViewById<TextView>(R.id.listtile_title)
        val startActivityButton = rowView.findViewById<Button>(R.id.listtile_button)

        fragment.itemClickListener( startActivityButton, activities, position )

        val image = rowView.findViewById<ImageView>(R.id.listtile_image)
        image.clipToOutline = true

        activityName.text = activities[position].activityName

        return rowView
    }
}

