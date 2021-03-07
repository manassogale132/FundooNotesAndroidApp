package com.example.myfirstapplication.UserData

class Label {

    var userID : String? = null
    var label : String? = null
    var creationTime : Long = System.currentTimeMillis()

    constructor(){

    }
    constructor(userID: String?,label: String?) {
        this.userID = userID
        this.label = label
    }
}