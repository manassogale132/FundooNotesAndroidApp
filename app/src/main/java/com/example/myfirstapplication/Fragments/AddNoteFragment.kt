package com.example.myfirstapplication.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.example.myfirstapplication.UserData.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_addnote.*

class AddNoteFragment : Fragment()  {

    private lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addnote,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()              //initialize auth object inside onCreate()
        database = FirebaseDatabase.getInstance()

       noteSaveBtn.setOnClickListener {
           addAndUpdateNotesToDataBase()
           getActivity()?.onBackPressed();
           hideKeyboard()
       }
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun addAndUpdateNotesToDataBase(){
        if(validationCheck()) {
            databaseReference = database?.reference!!.child("notes collection")
            val currentUserDb = databaseReference?.push()


            val userId = auth.currentUser?.uid
            val title = editTextTitle.editableText.toString()
            val description = editTextDescription.editableText.toString()

            val notes = Notes(userId,title, description)
            currentUserDb?.setValue(notes)

            Toast.makeText(activity, "Note save to database!", Toast.LENGTH_SHORT).show();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    fun validationCheck() : Boolean {                //entry validation check method
        var a = true
        if(editTextTitle.text.toString().trim().isEmpty()){
            editTextTitle.error = "Please enter title"
            a = false
        }
        return a
    }
    //------------------------------------------------------------------------------------------------------------------
    fun Fragment.hideKeyboard() {                  //to hide keyboard when save note button is clicked
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    //------------------------------------------------------------------------------------------------------------------

}