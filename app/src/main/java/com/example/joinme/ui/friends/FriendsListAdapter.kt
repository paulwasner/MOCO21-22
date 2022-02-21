package com.example.joinme.ui.friends

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.joinme.R
import com.example.joinme.datastructure.Friends

class FriendsListAdapter (
    private val context: Activity,
    private val friends: Array<Friends>,
    private val fragment: FriendsFragment
) : ArrayAdapter<Friends>(context, R.layout.friends_listtile , friends) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.friends_listtile, null, true)

        val friendsName = rowView.findViewById<TextView>(R.id.friends_listtile_title)
        val friendDetailButton = rowView.findViewById<TextView>(R.id.friends_listtile_button)

        val image = rowView.findViewById<ImageView>(R.id.friends_listtile_image)
        image.clipToOutline = true

        friendsName.text = friends[position].name
        fragment.checkActivityState( friendDetailButton, friends, position )


        return rowView
    }
}
