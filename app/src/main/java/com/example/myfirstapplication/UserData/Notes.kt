package com.example.myfirstapplication.UserData

import com.google.firebase.database.DatabaseReference

class Notes {
    var userID : String? = null
    var title : String? = null
    var description : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var noteId : String?=null

    constructor(){

    }
    constructor(userID: String?, title: String?, description: String?, noteId: String?) {
        this.userID = userID
        this.title = title
        this.description = description

        this.noteId = noteId
    }
}