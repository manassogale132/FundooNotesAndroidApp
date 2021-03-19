package com.example.myfirstapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.MyAdapter.MyArchivedNotesAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ArchiveFragment: Fragment()  {

    lateinit var recyclerviewArchivedNotesList : RecyclerView
    lateinit var myArchivedNotesAdapter: MyArchivedNotesAdapter
    lateinit var manager : LinearLayoutManager

    var databaseReference : DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archive,container,false)

        recyclerviewArchivedNotesList = view.findViewById(R.id.recyclerviewArchivedNotesList)
        manager = LinearLayoutManager(context)
        recyclerviewArchivedNotesList.layoutManager = manager

            loadDataIntoRecycler()

        return view
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun loadDataIntoRecycler() {
            val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
                .setQuery(FirebaseDatabase.getInstance().reference.child("notes collection")
                    .orderByChild("archived").equalTo(true), Notes::class.java)
                .build()

            myArchivedNotesAdapter = MyArchivedNotesAdapter(options)
            recyclerviewArchivedNotesList.adapter = myArchivedNotesAdapter
            myArchivedNotesAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        myArchivedNotesAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myArchivedNotesAdapter.stopListening()
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRightToDeleteItemFromRecyclerView()
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun swipeRightToDeleteItemFromRecyclerView(){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myArchivedNotesAdapter.deleteItem(viewHolder.adapterPosition)
                Toast.makeText(activity, "Note deleted!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerviewArchivedNotesList)
    }
    //------------------------------------------------------------------------------------------------------------------
}