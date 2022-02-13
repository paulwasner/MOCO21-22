package com.example.joinme

import androidx.lifecycle.ViewModel
import com.example.joinme.datastructure.User

class SharedViewModel : ViewModel() {
    var user = User("", "", "", "", "", "", "", mutableListOf())
    var uuid = ""
}