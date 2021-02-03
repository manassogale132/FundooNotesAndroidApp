package com.example.myfirstapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            registerUser.setOnClickListener {
                val intent = Intent(this,RegistrationActivity::class.java)
                Toast.makeText(this,"Register Page Opened!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }
}