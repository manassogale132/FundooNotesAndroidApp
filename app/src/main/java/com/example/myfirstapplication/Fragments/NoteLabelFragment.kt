package com.example.myfirstapplication.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.MyAdapter.MyLabelAdapter
import com.example.myfirstapplication.MyAdapter.MyLabelCheckBoxAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Label
import com.example.myfirstapplication.UserData.NoteLabelRelationShip
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_label_checkbox.*

class NoteLabelFragment: Fragment()   {

    lateinit var recyclerviewLabelCheckBox : RecyclerView
    lateinit var myLabelCheckBoxAdapter: MyLabelCheckBoxAdapter
    lateinit var manager : LinearLayoutManager

    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    var noteID : String? = null
    //------------------------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_label_checkbox, container, false)

        database = FirebaseDatabase.getInstance()

        arguments?.let {
            noteID = it.getString("noteId")
            Log.e("fragment", "onCreateView: ${it.get("noteId")} ")
        }

        recyclerviewLabelCheckBox = view.findViewById(R.id.recyclerviewLabelCheckBox)
        manager = LinearLayoutManager(context)
        recyclerviewLabelCheckBox.layoutManager = manager

        loadDataIntoRecycler()

        return view
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun loadDataIntoRecycler(){

        val options: FirebaseRecyclerOptions<Label> = FirebaseRecyclerOptions.Builder<Label>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("label collection")
                .orderByChild("creationTime"), Label::class.java)
            .build()

        myLabelCheckBoxAdapter = MyLabelCheckBoxAdapter(options) {
                position, label, isChecked ->

            databaseReference = database?.reference!!.child("notes_label_collection")

            if(isChecked) {
                val noteLabelRelationShipReference = databaseReference?.push()
                val relationShip = NoteLabelRelationShip(noteID, label.labelId)
                noteLabelRelationShipReference?.setValue(relationShip)
            }else{
                databaseReference?.get()?.addOnSuccessListener {
                    it.children.forEach { child ->
                        if(child.child("noteId").value!!.equals(noteID) && child.child("labelId").value!!.equals(label.labelId)){
                        databaseReference?.child(child.key.toString())?.removeValue()
                    }
                    }
                }
            }

        }
        myLabelCheckBoxAdapter.notifyDataSetChanged()
        recyclerviewLabelCheckBox.adapter = myLabelCheckBoxAdapter
    }
    override fun onStart() {
        super.onStart()
        myLabelCheckBoxAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myLabelCheckBoxAdapter.stopListening()
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backToAllNotesBtnFromLabels.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,
                NotesFragment()
            )?.commit()
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}