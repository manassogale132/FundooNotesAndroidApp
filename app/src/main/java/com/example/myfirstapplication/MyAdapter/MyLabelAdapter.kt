package com.example.myfirstapplication.MyAdapter

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus

class MyLabelAdapter(options: FirebaseRecyclerOptions<Label>) : FirebaseRecyclerAdapter<Label, MyLabelAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.label_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, label: Label) {
        holder.textLabel.text = label.label

        holder.deleteLabel.setOnClickListener {
            val builder : AlertDialog.Builder = AlertDialog.Builder(holder.textLabel.context)
            builder.setTitle("Delete Panel")
            builder.setMessage("Delete label?")

            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                getRef(position).key?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("label collection")
                        .child(it1).removeValue()
                }
            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
            })
            builder.show()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textLabel : TextView = itemView.findViewById(R.id.textViewLabelItem)
        var deleteLabel : ImageView = itemView.findViewById(R.id.deleteLabelBtn)
    }
}