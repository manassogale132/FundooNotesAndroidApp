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
import androidx.recyclerview.widget.*
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

        searchBtn.setOnClickListener {
            val searchText = searchEditText.editableText.toString()
            if(searchText.isNotEmpty()) {
                itemSearchInRecyclerView(searchText)
                searchEditText.clearFocus()
                hideKeyboard()
            }else if(searchText.isEmpty()){
                //searchEditText.error = "Search field is empty"
                Toast.makeText(activity, "Search field is empty! Enter something", Toast.LENGTH_SHORT).show()
            }
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

        myAdapter = MyAdapter(options)
        myAdapter.startListening()
        recyclerViewList.adapter = myAdapter
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