package com.example.myfirstapplication.MyAdapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Label
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

class MyLabelAdapter(options: FirebaseRecyclerOptions<Label>) : FirebaseRecyclerAdapter<Label, MyLabelAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.label_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, label: Label) {
        holder.textLabel.text = label.label

        Log.e("test", "onBindViewHolder: ${label.labelId}")


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

        holder.updateLabel.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.textLabel.context)
                .setContentHolder(ViewHolder(R.layout.dialog_content_label))
                .setExpanded(true,2300).create()

            val myView : View = dialogPlus.holderView
            val titleLabel : EditText = myView.findViewById(R.id.titleLabelDialog)
            val updateBTN : Button = myView.findViewById(R.id.updateButtonLabel)
            val close : ImageView = myView.findViewById(R.id.closeDialogLabel)

            titleLabel.setText(label.label)
            dialogPlus.show()

            close.setOnClickListener {
                dialogPlus.dismiss()
                titleLabel.clearFocus()
            }

            updateBTN.setOnClickListener {
                val map: MutableMap<String, Any> = HashMap()
                map["label"] = titleLabel.text.toString()

                getRef(position).key?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("label collection")
                        .child(it1).updateChildren(map)
                        .addOnSuccessListener {
                            dialogPlus.dismiss()
                            titleLabel.clearFocus()
                            Toast.makeText(myView.getContext(), "Label Updated!", Toast.LENGTH_SHORT).show();
                        }
                        .addOnFailureListener {
                            dialogPlus.dismiss()
                            Toast.makeText(myView.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textLabel : TextView = itemView.findViewById(R.id.textViewLabelItem)
        var deleteLabel : ImageView = itemView.findViewById(R.id.deleteLabelBtn)
        var updateLabel : ImageView = itemView.findViewById(R.id.updateLabelBtn)
    }
}