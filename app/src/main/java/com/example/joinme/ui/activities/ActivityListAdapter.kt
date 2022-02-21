package com.example.joinme.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.joinme.R
import com.example.joinme.datastructure.Activity as ActivityData


class ActivityListAdapter(
    private val context: Activity,
    private val activities: Array<ActivityData>,
    private val fragment: ActivitiesFragment
) :
    ArrayAdapter<ActivityData>(context, R.layout.activities_listtile, activities) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.activities_listtile, null, true)

        val activityName = rowView.findViewById<TextView>(R.id.listtile_title)
        val startActivityButton = rowView.findViewById<Button>(R.id.listtile_button)

        fragment.updateButtonOnStartUp(startActivityButton, activities, position)
        fragment.itemClickListener(startActivityButton, activities, position)

        val image = rowView.findViewById<ImageView>(R.id.listtile_image)
        image.clipToOutline = true

        activityName.text = activities[position].activityName

        return rowView
    }
}