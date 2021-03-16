package com.example.myfirstapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.myfirstapplication.MyAdapter.MyAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_notes.*


class NotesFragment : Fragment() {

    lateinit var recyclerViewList : RecyclerView
    lateinit var myAdapter: MyAdapter
    lateinit var manager : LinearLayoutManager

    var databaseReference : DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view :View = inflater.inflate(R.layout.fragment_notes,container,false)
        recyclerViewList = view.findViewById(R.id.recyclerViewList)
        manager = LinearLayoutManager(context)
        recyclerViewList.layoutManager = manager

        databaseReference?.keepSynced(true) //offline support

        loadDataIntoRecycler()

        return view
    }

    private fun loadDataIntoRecycler(){

        val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("notes collection")
                .orderByChild("creationTime"), Notes::class.java)
            .build()

        myAdapter = MyAdapter(options) { position : Int, note : Notes ->
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,
                NoteLabelFragment().also {
                    it.arguments = bundleOf("noteId" to note.noteId)
                }
            )?.commit()
        }
        myAdapter.notifyDataSetChanged()
        recyclerViewList.adapter = myAdapter
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

        searchViewId.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchText: String): Boolean {
                return false
            }
            override fun onQueryTextChange(searchText: String): Boolean {
                itemSearchInRecyclerView(searchText)
               return false
            }
        })
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun swipeRightToDeleteItemFromRecyclerView(){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.deleteItem(viewHolder.adapterPosition)
                Toast.makeText(activity, "Note deleted!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerViewList)
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun addClickToGridButton(){
        var i : Int =0
        floatingBtnToGrid.setOnClickListener {
            if(i == 0) {
                // recyclerViewList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                recyclerViewList.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                Toast.makeText(activity, "Switched to grid view!", Toast.LENGTH_SHORT).show();
                i++
            }
            else if (i == 1){
                recyclerViewList.layoutManager = LinearLayoutManager(context)
                Toast.makeText(activity, "Switched to list view!", Toast.LENGTH_SHORT).show();
                i = 0
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private fun itemSearchInRecyclerView(searchText : String){    //search feature

        val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("notes collection")
                .orderByChild("title").startAt(searchText).endAt(searchText+"\uf8ff"), Notes::class.java)
            .build()

        myAdapter = MyAdapter(options) { position : Int, note : Notes ->
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container,
                NoteLabelFragment().also {
                    it.arguments = bundleOf("noteId" to note.noteId)
                }
            )?.commit()
        }
        myAdapter.startListening()
        recyclerViewList.adapter = myAdapter
    }
    //------------------------------------------------------------------------------------------------------------------
}