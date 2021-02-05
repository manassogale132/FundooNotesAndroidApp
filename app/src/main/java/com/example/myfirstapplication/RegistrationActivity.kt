package com.example.myfirstapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.password
import kotlinx.android.synthetic.main.activity_registration.showPassword

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        alreadyUser.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this, "Login Page Opened!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
//---------------------------------------------------------------------------------------------------------------------------------------
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
//---------------------------------------------------------------------------------------------------------------------------------------
    }
}