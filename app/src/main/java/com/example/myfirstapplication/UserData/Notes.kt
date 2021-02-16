package com.example.myfirstapplication.UserData

class Notes {

    var title : String? = null
    var description : String? = null

    constructor(){

    }
    constructor(title: String?, description: String?) {
        this.title = title
        this.description = description
    }
}