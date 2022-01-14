package com.example.joinme.datastructure

class Participants(val min: Int,val max: Int)

class Activity(val activityName: String, val participants: Participants, var started: Boolean)

class Friends(val id: Int, val name: String)