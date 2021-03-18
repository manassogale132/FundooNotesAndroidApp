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

class MyArchivedNotesAdapter(options: FirebaseRecyclerOptions<Notes>): FirebaseRecyclerAdapter<Notes, MyArchivedNotesAdapter.MyViewHolder>(options)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view_archived_notes,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Notes) {
        holder.textViewArchivedTitleItem.text = model.title
        holder.textViewArchivedDescriptionItem.text = model.description
    }

    public fun deleteItem(position : Int){
        snapshots.getSnapshot(position).ref.removeValue()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textViewArchivedTitleItem : TextView = itemView.findViewById(R.id.textViewArchivedTitleItem)
        var textViewArchivedDescriptionItem : TextView = itemView.findViewById(R.id.textViewArchivedDescriptionItem)
    }
}