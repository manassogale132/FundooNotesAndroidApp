package com.example.myfirstapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.myfirstapplication.R
import com.example.myfirstapplication.UserData.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.passwordR
import kotlinx.android.synthetic.main.activity_registration.showPassword

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth            //global variable - object of FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()              //initialize auth object inside onCreate()
        database = FirebaseDatabase.getInstance()

        showPasswordCheckBox()

        registerButton.setOnClickListener {
            signUpUserValidation()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun showPasswordCheckBox() {
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                passwordR.transformationMethod = PasswordTransformationMethod.getInstance();
            } else {
                passwordR.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
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

                    val currentUser = auth.currentUser
                    databaseReference= database?.reference!!.child("user profiles")
                    val currentUserDb = databaseReference?.child((currentUser?.uid!!))

                    val fullname = fullName.editableText.toString()
                    val email = emailR.editableText.toString()

                    val users = Users(fullname, email)
                    currentUserDb?.setValue(users)

                    Toast.makeText(baseContext, "User Registered", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed. Try again", Toast.LENGTH_SHORT).show()
                    progressBarR.visibility = View.INVISIBLE
                }
            }
    }
    //-------------------------------------------------------------------------------------------------------------------------
}