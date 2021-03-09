package com.example.myfirstapplication.UserData

class Label {

    var userID : String? = null
    var label : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var labelId : String?=null

    constructor(){

    }
    constructor(userID: String?,label: String?,labelId: String?) {
        this.userID = userID
        this.label = label
        this.labelId = labelId
    }
}