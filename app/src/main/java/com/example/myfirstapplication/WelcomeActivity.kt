package com.example.myfirstapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity  : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        logOutbutton.setOnClickListener {
            logoutMethod()
        }
    }

    fun logoutMethod() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(baseContext, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}