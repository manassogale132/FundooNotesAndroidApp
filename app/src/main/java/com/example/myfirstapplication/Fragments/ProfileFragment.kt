package com.example.myfirstapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myfirstapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference= database?.reference!!.child("user profiles")
        val currentUser = auth.currentUser

        displayUserInformation(currentUser)
    }
    //---------------------------------------------------------------------------------------------------------------------
    private fun displayUserInformation(currentUser: FirebaseUser?) {
        val userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(currentUser != null) {
                    Glide.with(this@ProfileFragment).load(currentUser?.photoUrl).error(R.drawable.default_user_image).into(imageView)
                    fullNameTextViewCustom.text = snapshot.child("fullname").value.toString()
                    emailTextViewCuston.text = snapshot.child("email").value.toString()
                    uid.text = currentUser?.uid
                }
            }

        })
    }
    //---------------------------------------------------------------------------------------------------------------------
}