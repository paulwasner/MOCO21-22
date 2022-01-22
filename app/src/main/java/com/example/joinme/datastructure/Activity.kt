package com.example.joinme.datastructure

import java.io.Serializable

class Activity(val activityName: String, var started: Boolean)

class Friends(val id: Int, val name: String)

data class User(
    val email: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val location: String? = null,
    val activityState: String? = null,
    val activityName: String? = null,
    val friends: MutableList<String>?
): Serializable