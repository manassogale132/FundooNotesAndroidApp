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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.MyAdapter.MyAdapter
import com.example.myfirstapplication.MyAdapter.MyLabelAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Label
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragment_addnote.*
import kotlinx.android.synthetic.main.fragment_label.*

class LabelFragment : Fragment()  {

    private lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    lateinit var recyclerviewLabel : RecyclerView
    lateinit var myLabelAdapter: MyLabelAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_label,container,false)

        recyclerviewLabel = view.findViewById(R.id.recyclerviewLabel)
        manager = LinearLayoutManager(context)
        recyclerviewLabel.layoutManager = manager

        loadDataIntoRecycler()

        return view
    }

    private fun loadDataIntoRecycler(){

        val options: FirebaseRecyclerOptions<Label> = FirebaseRecyclerOptions.Builder<Label>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("label collection"), Label::class.java)
            .build()

        myLabelAdapter = MyLabelAdapter(options)
        myLabelAdapter.notifyDataSetChanged()
        recyclerviewLabel.adapter = myLabelAdapter
    }

    override fun onStart() {
        super.onStart()
        myLabelAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myLabelAdapter.stopListening()
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()              //initialize auth object inside onCreate()
        database = FirebaseDatabase.getInstance()

        labelSaveBtn.setOnClickListener {
            if(validationCheck()) {
                addLabelsToDataBase()
                editTextLabel.clearFocus()
                hideKeyboard()
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun addLabelsToDataBase(){
        databaseReference = database?.reference!!.child("label collection")
        val currentUserDb = databaseReference?.push()


        val userId = auth.currentUser?.uid
        val label = editTextLabel.editableText.toString()

        val labelentry = Label(userId,label)
        currentUserDb?.setValue(labelentry)

        Toast.makeText(activity, "Label saved!", Toast.LENGTH_SHORT).show();
    }
    //------------------------------------------------------------------------------------------------------------------
    fun validationCheck() : Boolean {                //entry validation check method
        var a = true
        if(editTextLabel.text.toString().trim().isEmpty()){
            editTextLabel.error = "Please enter label"
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