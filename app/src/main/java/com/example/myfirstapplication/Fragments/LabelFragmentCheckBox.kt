package com.example.myfirstapplication.Fragments

import android.os.Bundle
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
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class LabelFragmentCheckBox: Fragment()   {

    lateinit var recyclerviewLabelCheckBox : RecyclerView
    lateinit var myLabelCheckBoxAdapter: MyLabelCheckBoxAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_label_checkbox, container, false)

        recyclerviewLabelCheckBox = view.findViewById(R.id.recyclerviewLabelCheckBox)
        manager = LinearLayoutManager(context)
        recyclerviewLabelCheckBox.layoutManager = manager

        loadDataIntoRecycler()

        return view
    }

    private fun loadDataIntoRecycler(){

        val options: FirebaseRecyclerOptions<Label> = FirebaseRecyclerOptions.Builder<Label>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("label collection")
                .orderByChild("creationTime"), Label::class.java)
            .build()

        myLabelCheckBoxAdapter = MyLabelCheckBoxAdapter(options)
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

}