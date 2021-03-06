package com.example.myfirstapplication.MyAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.firebase.ui.database.paging.LoadingState
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlin.collections.HashMap
import kotlin.coroutines.coroutineContext

class MyAdapter(options: DatabasePagingOptions<Notes>) : FirebaseRecyclerPagingAdapter<Notes, MyAdapter.MyViewHolder>(options) {

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
                .setExpanded(true,2300).create()

            val myView : View = dialogPlus.holderView
            val title : EditText = myView.findViewById(R.id.titleDialog)
            val description : EditText = myView.findViewById(R.id.descriptionDialog)
            val updateBTN : Button = myView.findViewById(R.id.updateButton)
            val close : ImageView = myView.findViewById(R.id.closeDialog)

            title.setText(notes.title)
            description.setText(notes.description)

            dialogPlus.show()

            close.setOnClickListener {
                dialogPlus.dismiss()
                title.clearFocus()
                description.clearFocus()
            }

            updateBTN.setOnClickListener {
                val map: MutableMap<String, Any> = HashMap()
                map["title"] = title.text.toString()
                map["description"] = description.text.toString()

                getRef(p1).key?.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("notes collection")
                        .child(it1).updateChildren(map)
                        .addOnSuccessListener {
                            dialogPlus.dismiss()
                            title.clearFocus()
                            description.clearFocus()
                            Toast.makeText(myView.getContext(), "Note Updated!", Toast.LENGTH_SHORT).show();
                        }
                        .addOnFailureListener {
                            dialogPlus.dismiss()
                            Toast.makeText(myView.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        }
    }

   /*public fun deleteItem(position : Int){
        snapshots.getSnapshot(position).ref.removeValue()
    }*/

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textTitle : TextView = itemView.findViewById(R.id.textViewTitleItem)
        var textDescription : TextView = itemView.findViewById(R.id.textViewDescriptionItem)
        var updateBtn : ImageView = itemView.findViewById(R.id.updateICon)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        when (state) {
            LoadingState.LOADED -> {
                Log.d("PaggingLog","Total Notes Loaded" + itemCount)
            }
            LoadingState.ERROR -> {
                Log.d("PaggingLog","Error Loading Notes")
            }
            LoadingState.FINISHED ->{
               //Toast.makeText(this, "All Notes Loaded!", Toast.LENGTH_SHORT).show();
                Log.d("PaggingLog","All Notes Loaded")
            }
            LoadingState.LOADING_INITIAL -> {
                Log.d("PaggingLog","Loading Initial Notes")
            }
            LoadingState.LOADING_MORE -> {
                Log.d("PaggingLog","Loading Next Notes")
            }
        }
    }
}




