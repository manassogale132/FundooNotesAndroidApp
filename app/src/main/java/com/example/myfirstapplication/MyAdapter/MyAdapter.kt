package com.example.myfirstapplication.MyAdapter

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.allyants.notifyme.NotifyMe
import com.example.myfirstapplication.Activities.DashboardActivity
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.NoteLabelRelationShip
import com.example.myfirstapplication.UserData.Notes
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MyAdapter(options: FirebaseRecyclerOptions<Notes>,
    val onLabelItemClicked: (position: Int, note: Notes) -> Unit) : FirebaseRecyclerAdapter<Notes, MyAdapter.MyViewHolder>(options) {

    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    val cal : Calendar = Calendar.getInstance()
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var seconds = 0

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.myfirstapplication.Fragments"
    private val desctiption = "Reminder Test Notification"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: MyViewHolder, p1: Int, note: Notes) {

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("notes_label_collection")

        holder.textTitle.text = note.title
        holder.textDescription.text = note.description

        databaseReference?.get()?.addOnSuccessListener {
            it.children.forEach { child ->
                if(child.child("noteId").value!!.equals(note.noteId)){
                    databaseReference?.child(child.key.toString())?.removeValue()

                    Log.e("Child", "onBindViewHolder NoteID : ${note.noteId} ${child.value}")
                }
            }
        }


        Log.e("test", "onBindViewHolder: ${note.noteId}")
        //------------------------------------------------------------------------------------------------------------------
        holder.updateBtn.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.textTitle.context)
                .setContentHolder(ViewHolder(R.layout.dialog_content))
                .setExpanded(true, 2000).create()

            val myView: View = dialogPlus.holderView
            val title: EditText = myView.findViewById(R.id.titleDialog)
            val description: EditText = myView.findViewById(R.id.descriptionDialog)
            val updateBTN: Button = myView.findViewById(R.id.updateButton)
            val close: ImageView = myView.findViewById(R.id.closeDialog)

            title.setText(note.title)
            description.setText(note.description)

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
                    FirebaseDatabase.getInstance().reference.child("notes collection").child(it1).updateChildren(map)
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
        //------------------------------------------------------------------------------------------------------------------
        holder.loadLabelFragment.setOnClickListener {
            onLabelItemClicked(p1, note)
        }
        //------------------------------------------------------------------------------------------------------------------
        holder.remainderBellBtn.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.textTitle.context)
                .setContentHolder(ViewHolder(R.layout.dialog_reminder_date_time_picker))
                .setExpanded(true, 2000).create()

            val myView: View = dialogPlus.holderView
            val dateTimePickerBtn: Button = myView.findViewById(R.id.dateTimePickerBtn)
            val textViewTimeDate: TextView = myView.findViewById(R.id.textViewTimeDate)
            val addNoteItemToReminderBtn: Button = myView.findViewById(R.id.addNoteItemToReminderBtn)
            val closeReminderNoteDialog: ImageView = myView.findViewById(R.id.closeReminderNoteDialog)

            dialogPlus.show()

            closeReminderNoteDialog.setOnClickListener {
                dialogPlus.dismiss()
            }

            dateTimePickerBtn.setOnClickListener {

                day = cal.get(Calendar.DAY_OF_MONTH)
                month = cal.get(Calendar.MONTH)
                year = cal.get(Calendar.YEAR)
                hour = cal.get(Calendar.HOUR)
                minute = cal.get(Calendar.MINUTE)
                seconds = cal.get(Calendar.SECOND)


                DatePickerDialog(it.context,object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                        cal.set(Calendar.MONTH,month)
                        cal.set(Calendar.YEAR,year)

                        val currentDateString : String = DateFormat.getDateInstance().format(cal.time)

                        TimePickerDialog(it.context,object : TimePickerDialog.OnTimeSetListener {
                            @SuppressLint("SimpleDateFormat")
                            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                                cal.set(Calendar.HOUR,hourOfDay)
                                cal.set(Calendar.MINUTE,minute)
                                cal.set(Calendar.SECOND,0)

                                val format = SimpleDateFormat("k:mm aa")
                                val time : String = format.format(cal.time)

                                textViewTimeDate.text = "$currentDateString, $time"

                                val notifyMe: NotifyMe.Builder = NotifyMe.Builder(view?.context)
                                notifyMe.title("Title : ${note.title} (Reminder!) ");
                                notifyMe.content("Description : ${note.description}");
                                notifyMe.color( 225,225,225,225);//Color of notification header
                                notifyMe.time(cal);//The time to popup notification
                                notifyMe.addAction(Intent(),"Dismiss",true,false)
                                notifyMe.small_icon(R.mipmap.ic_launcher)
                                notifyMe.build()
                            }
                        },hour,minute,true).show()
                    }
                } ,year,month,day).show()
            }

            addNoteItemToReminderBtn.setOnClickListener {
                auth = FirebaseAuth.getInstance()
                database = FirebaseDatabase.getInstance()
                notificationManager = myView.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                getRef(p1).key?.let { it1 ->
                    databaseReference = database?.reference!!.child("reminder notes collection").child(it1)

                    val userId = auth.currentUser?.uid
                    val title = note.title
                    val description = note.description
                    val noteId = databaseReference?.key
                    val noteReminderTimeDate = textViewTimeDate.text.toString()
                    val archived = false
                    val notes = Notes(userId, title, description, noteId,noteReminderTimeDate,archived)

                    databaseReference?.setValue(notes)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationChannel = NotificationChannel(
                            channelId,
                            desctiption,
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        notificationChannel.enableVibration(false)
                        notificationManager.createNotificationChannel(notificationChannel)

                        this.builder = Notification.Builder(myView.context, channelId)
                            .setContentTitle("Fundoo Notes Alert.")
                            .setContentText("Note added to reminder list!")
                            .setSmallIcon(R.mipmap.ic_launcher)
                    } else {
                        this.builder = Notification.Builder(myView.context)
                            .setContentTitle("Fundoo Notes Alert.")
                            .setContentText("Note added to reminder list!")
                            .setSmallIcon(R.mipmap.ic_launcher)
                    }
                    notificationManager.notify(1234, this.builder.build())
                    Toast.makeText(myView.context, "Added to reminder list!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        //------------------------------------------------------------------------------------------------------------------
        holder.archiveBtn.setOnClickListener {
            val map: MutableMap<String, Any> = HashMap()
            map["archived"] = true

            getRef(p1).key?.let { it1 ->
                FirebaseDatabase.getInstance().reference.child("notes collection").child(it1)
                    .updateChildren(map)
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).ref.removeValue()
    }
    //------------------------------------------------------------------------------------------------------------------
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTitle: TextView = itemView.findViewById(R.id.textViewTitleItem)
        var textDescription: TextView = itemView.findViewById(R.id.textViewDescriptionItem)
        var updateBtn: ImageView = itemView.findViewById(R.id.updateICon)
        var loadLabelFragment: ImageView = itemView.findViewById(R.id.addLabelToItemBtn)
        var remainderBellBtn: ImageButton = itemView.findViewById(R.id.remainderBellBtn)
        var archiveBtn : ImageButton = itemView.findViewById(R.id.archiveBtn)
    }
    //------------------------------------------------------------------------------------------------------------------
}






