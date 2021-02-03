package com.example.myfirstapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        alreadyUser.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            Toast.makeText(this,"Login Page Opened!",Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }
}