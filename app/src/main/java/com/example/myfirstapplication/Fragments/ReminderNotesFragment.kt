package com.example.myfirstapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.MyAdapter.MyAdapter
import com.example.myfirstapplication.MyAdapter.MyLabelCheckBoxAdapter
import com.example.myfirstapplication.MyAdapter.MyReminderNotesAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Label
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_label_checkbox.*
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_remainder.*
import kotlinx.android.synthetic.main.fragment_remainder.floatingBtnToGrid

class ReminderNotesFragment : Fragment()  {

    lateinit var recyclerviewReminderNotesList : RecyclerView
    lateinit var myReminderNotesAdapter: MyReminderNotesAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_remainder,container,false)

        recyclerviewReminderNotesList = view.findViewById(R.id.recyclerviewReminderNotesList)
        manager = LinearLayoutManager(context)
        recyclerviewReminderNotesList.layoutManager = manager

        loadDataIntoRecycler()

        return view
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun loadDataIntoRecycler() {

        val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("reminder notes collection")
                .orderByChild("creationTime"), Notes::class.java).build()

        myReminderNotesAdapter = MyReminderNotesAdapter(options)
        recyclerviewReminderNotesList.adapter = myReminderNotesAdapter
    }
    override fun onStart() {
        super.onStart()
        myReminderNotesAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myReminderNotesAdapter.stopListening()
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRightToDeleteItemFromRecyclerView()
        addClickToGridButton()
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun swipeRightToDeleteItemFromRecyclerView(){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myReminderNotesAdapter.deleteItem(viewHolder.adapterPosition)
                Toast.makeText(activity, "Note deleted from reminder list!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerviewReminderNotesList)
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun addClickToGridButton(){
        var i : Int =0
        floatingBtnToGrid.setOnClickListener {
            if(i == 0) {
                // recyclerViewList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                recyclerviewReminderNotesList.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                Toast.makeText(activity, "Switched to grid view!", Toast.LENGTH_SHORT).show();
                i++
            }
            else if (i == 1){
                recyclerviewReminderNotesList.layoutManager = LinearLayoutManager(context)
                Toast.makeText(activity, "Switched to list view!", Toast.LENGTH_SHORT).show();
                i = 0
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------

}