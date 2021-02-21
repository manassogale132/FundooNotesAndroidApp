package com.example.myfirstapplication.MyAdapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.Fragments.NotesFragment
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.DialogPlusBuilder
import com.orhanobut.dialogplus.ViewHolder
import java.util.*
import kotlin.collections.HashMap

class MyAdapter(options: FirebaseRecyclerOptions<Notes>) : FirebaseRecyclerAdapter<Notes, MyAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view,parent,false)
        return  MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder,p1: Int, notes: Notes) {
        holder.textTitle.text = notes.title
        holder.textDescription.text = notes.description

        holder.updateBtn.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.textTitle.context)
                .setContentHolder(ViewHolder(R.layout.dialog_content))
                .setExpanded(true,1900).create()

            val myView : View = dialogPlus.holderView
            val title : EditText = myView.findViewById(R.id.titleDialog)
            val description : EditText = myView.findViewById(R.id.descriptionDialog)
            val updateBTN : Button = myView.findViewById(R.id.updateButton)

            title.setText(notes.title)
            description.setText(notes.description)

            dialogPlus.show()

            updateBTN.setOnClickListener {
                val map: MutableMap<String, Any> = HashMap()
                map["title"] = title.text.toString()
                map["description"] = description.text.toString()

                getRef(p1).key?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("notes collection")
                        .child(it1).updateChildren(map)
                        .addOnSuccessListener {
                            dialogPlus.dismiss()
                        }
                        .addOnFailureListener {
                            dialogPlus.dismiss()
                        }
                }
            }
        }
    }

    public fun deleteItem(position : Int){
        snapshots.getSnapshot(position).ref.removeValue()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textTitle : TextView = itemView.findViewById(R.id.textViewTitleItem)
        var textDescription : TextView = itemView.findViewById(R.id.textViewDescriptionItem)
        var updateBtn : ImageView = itemView.findViewById(R.id.updateICon)
    }
}




