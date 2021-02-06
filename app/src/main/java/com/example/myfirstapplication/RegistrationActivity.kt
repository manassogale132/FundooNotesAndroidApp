package com.example.myfirstapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.passwordR
import kotlinx.android.synthetic.main.activity_registration.showPassword

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth            //global variable - object of FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()              //initialize auth object inside onCreate()

        registerPageToLoginPage()
        showPasswordCheckBox()

        registerButton.setOnClickListener {
            signUpUserValidation()
        }
    }

    private fun registerPageToLoginPage() {
        alreadyUser.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this, "Login Page Opened!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }

    private fun showPasswordCheckBox() {
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                passwordR.transformationMethod = PasswordTransformationMethod.getInstance();
            } else {
                passwordR.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
        }
    }

    private fun signUpUserValidation() {               //validation and signup method

        if (fullName.text.toString().isEmpty()) {
            fullName.error = "Please enter Fullname"
            fullName.requestFocus()
            return
        }

        if(fullName.text.toString().matches("[0-9*$%#&^()@!_+{}';]*".toRegex())) {
            fullName.error = "Please enter proper Fullname"
            fullName.requestFocus()
            return
        }

        if (emailR.text.toString().isEmpty()) {
            emailR.error = "Please enter Email"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailR.text.toString()).matches()) {
            emailR.error = "Please enter Valid Email"
            emailR.requestFocus()
            return
        }
        if (passwordR.text.toString().isEmpty()) {
            passwordR.error = "Please enter Password"
            passwordR.requestFocus()
            return
        }
        if (passwordR.length() < 6) {
            passwordR.setError("Password must be > 6 characters")
            return
        }

        progressBarR.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(emailR.text.toString(), passwordR.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "User Registered", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed. Try again", Toast.LENGTH_SHORT).show()
                }
            }
    }
}