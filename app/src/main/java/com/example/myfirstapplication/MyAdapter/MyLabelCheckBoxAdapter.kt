package com.example.myfirstapplication.MyAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Label
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyLabelCheckBoxAdapter (options: FirebaseRecyclerOptions<Label>) : FirebaseRecyclerAdapter<Label, MyLabelCheckBoxAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.label_item_checkbox,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int, model: Label) {
        holder.textLabelCheckBox.text = model.label
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textLabelCheckBox : TextView = itemView.findViewById(R.id.textViewLabelItemCheckBox)
        var labelCheckBox : TextView = itemView.findViewById(R.id.labelCheckBox)
    }
}