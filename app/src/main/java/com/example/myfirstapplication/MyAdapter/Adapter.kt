package com.example.myfirstapplication.MyAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyAdapter(options: FirebaseRecyclerOptions<Notes>) : FirebaseRecyclerAdapter<Notes, MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view,parent,false)
        return  MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, p1: Int, notes: Notes) {
        holder.textTitle.text = notes.title
        holder.textDescription.text = notes.description
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var textTitle = itemView.findViewById<TextView>(R.id.textViewTitleItem)
    var textDescription = itemView.findViewById<TextView>(R.id.textViewDescriptionItem)
}