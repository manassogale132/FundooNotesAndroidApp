package com.example.myfirstapplication.UserData

class NoteLabelRelationShip {

    var noteId : String?=null
    var labelId : String? = null

    constructor(){

    }
    constructor(noteId: String?,labelId: String?) {
        this.noteId = noteId
        this.labelId = labelId
    }
}