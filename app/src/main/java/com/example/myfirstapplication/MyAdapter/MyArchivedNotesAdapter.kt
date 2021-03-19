package com.example.myfirstapplication.MyAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyArchivedNotesAdapter(options: FirebaseRecyclerOptions<Notes>): FirebaseRecyclerAdapter<Notes, MyArchivedNotesAdapter.MyViewHolder>(options)  {

    var databaseReference : DatabaseReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view_archived_notes,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, p1: Int, model: Notes) {
        holder.textViewArchivedTitleItem.text = model.title
        holder.textViewArchivedDescriptionItem.text = model.description

        holder.unarchiveIcon.setOnClickListener {
            val map: MutableMap<String, Any> = HashMap()
            map["archived"] = false

            Log.e("itemcount", "onBindViewHolder: ${itemCount} " )
            Log.e("itemcount", "onBindViewHolder: ${p1} " )

            FirebaseDatabase.getInstance().reference.child("notes collection").child(model.noteId!!)
                .updateChildren(map)

            Toast.makeText(it.context, " Note Unarchived!", Toast.LENGTH_SHORT).show();
        }
    }

    public fun deleteItem(p1 : Int){
        snapshots.getSnapshot(p1).ref.removeValue()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textViewArchivedTitleItem : TextView = itemView.findViewById(R.id.textViewArchivedTitleItem)
        var textViewArchivedDescriptionItem : TextView = itemView.findViewById(R.id.textViewArchivedDescriptionItem)
        var unarchiveIcon : ImageButton = itemView.findViewById(R.id.unarchiveIcon)
    }
}