package com.example.myfirstapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myfirstapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        displayUserInformation(currentUser)
    }
    //---------------------------------------------------------------------------------------------------------------------
    private fun displayUserInformation(currentUser: FirebaseUser?) {

        fullNameTextViewCustom.text = currentUser?.displayName
        emailTextViewCuston.text = currentUser?.email
        uid.text = currentUser?.uid
        if (currentUser != null) {
            Glide.with(this).load(currentUser.photoUrl).into(imageView)
        }
    }
    //---------------------------------------------------------------------------------------------------------------------
}