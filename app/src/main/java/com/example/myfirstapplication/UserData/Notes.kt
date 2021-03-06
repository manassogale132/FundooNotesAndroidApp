package com.example.myfirstapplication.UserData

class Notes {
    var userID : String? = null
    var title : String? = null
    var description : String? = null
    var creationTime : Long = System.currentTimeMillis()

    constructor(){

    }
    constructor(userID: String?,title: String?, description: String?) {
        this.userID = userID
        this.title = title
        this.description = description
    }
}