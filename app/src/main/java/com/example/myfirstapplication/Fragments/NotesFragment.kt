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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myfirstapplication.MyAdapter.MyAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_addnote.*
import kotlinx.android.synthetic.main.fragment_notes.*


class NotesFragment : Fragment()  {

    lateinit var recyclerViewList : RecyclerView
    lateinit var myAdapter: MyAdapter

    var databaseReference : DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view :View = inflater.inflate(R.layout.fragment_notes,container,false)
        recyclerViewList = view.findViewById(R.id.recyclerViewList)
        recyclerViewList.layoutManager = LinearLayoutManager(context)

        val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("notes collection"), Notes::class.java)
            .build()

        databaseReference?.keepSynced(true) //offline support

        myAdapter = MyAdapter(options)
        myAdapter.notifyDataSetChanged()
        recyclerViewList.adapter = myAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRightToDeleteItemFromRecyclerView()
        addClickToGridButton()
        addClickToLinearListButton()

        searchBtn.setOnClickListener {
            val searchText = searchEditText.editableText.toString()
            itemSearchInRecyclerView(searchText)
            searchEditText.clearFocus()
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun swipeRightToDeleteItemFromRecyclerView(){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.deleteItem(viewHolder.adapterPosition)
                Toast.makeText(activity, "Note deleted from database!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerViewList)
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun addClickToGridButton(){
        floatingBtnToGrid.setOnClickListener {
            recyclerViewList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun addClickToLinearListButton(){
        floatingBtnToGrid.setOnLongClickListener {
            recyclerViewList.layoutManager = LinearLayoutManager(context)
            return@setOnLongClickListener true
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun itemSearchInRecyclerView(searchText : String){

        val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("notes collection").orderByChild("title").startAt(searchText).endAt(searchText+"\uf8ff"), Notes::class.java)
            .build()

        myAdapter = MyAdapter(options)
        myAdapter.startListening()
        recyclerViewList.adapter = myAdapter
    }
    //------------------------------------------------------------------------------------------------------------------

}