package com.example.joinme.datastructure

import java.io.Serializable

class Activity(val activityName: String, var started: Boolean)

class Friends(val id: String, val name: String) {
    override fun toString(): String {
        return "ID: $id | Name: $name"
    }
}

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