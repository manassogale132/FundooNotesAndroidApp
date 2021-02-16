package com.example.myfirstapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.MyAdapter.MyAdapter
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class NotesFragment : Fragment()  {

    lateinit var recyclerViewList : RecyclerView
    lateinit var myAdapter: MyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view :View = inflater.inflate(R.layout.fragment_notes,container,false)

        recyclerViewList = view.findViewById(R.id.recyclerViewList)
        recyclerViewList.layoutManager = LinearLayoutManager(context)

        val options: FirebaseRecyclerOptions<Notes> = FirebaseRecyclerOptions.Builder<Notes>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("notes collection"), Notes::class.java)
            .build()

        myAdapter = MyAdapter(options)
        myAdapter.startListening()
        myAdapter.notifyDataSetChanged()
        recyclerViewList.adapter = myAdapter
        return view
    }
}