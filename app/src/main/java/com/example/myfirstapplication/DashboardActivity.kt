package com.example.myfirstapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity  : AppCompatActivity()  {

    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        displayUserInformation(currentUser)

        logOutbutton.setOnClickListener {
            logoutMethod()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun displayUserInformation(currentUser: FirebaseUser?) {
        emailTextView.text = currentUser?.email
        nameTextView.text = currentUser?.displayName
    }
    //-------------------------------------------------------------------------------------------------------------------------
    fun logoutMethod() {
        auth.signOut();
        Toast.makeText(baseContext, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    //-------------------------------------------------------------------------------------------------------------------------
}