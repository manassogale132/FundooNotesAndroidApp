package com.example.myfirstapplication.MyAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Label
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_addnote.*

class MyLabelCheckBoxAdapter (options: FirebaseRecyclerOptions<Label>,val onLabelItemChecked : (position : Int, label : Label,isChecked : Boolean)-> Unit) : FirebaseRecyclerAdapter<Label, MyLabelCheckBoxAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.label_item_checkbox,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int, model: Label) {
        holder.textLabelCheckBox.text = model.label

        holder.labelCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onLabelItemChecked.invoke(position,model,isChecked)
        }

        Log.e("testLabelCheckBox", "onBindViewHolder: ${model.labelId}")
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textLabelCheckBox : TextView = itemView.findViewById(R.id.textViewLabelItemCheckBox)
        var labelCheckBox : CheckBox = itemView.findViewById(R.id.labelCheckBox)
    }
}