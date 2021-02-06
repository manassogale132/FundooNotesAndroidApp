package com.example.myfirstapplication

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.showPassword
import kotlinx.android.synthetic.main.activity_registration.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        loginPageToRegisterPage()
        showPasswordCheckBox()

        loginButton.setOnClickListener {
            loginUserValidation()
        }
    }

    private fun loginPageToRegisterPage() {
        registerUser.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            Toast.makeText(this, "Register Page Opened!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }

    private fun showPasswordCheckBox() {
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                password.transformationMethod = PasswordTransformationMethod.getInstance();
            } else {
                password.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
        }
    }

    private fun loginUserValidation(){

        if (email.text.toString().isEmpty()) {
            email.error = "Please enter Email"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter Valid Email"
            email.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter Password"
            password.requestFocus()
            return
        }
        if (password.length() < 6) {
            password.setError("Password must be > 6 characters")
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Wrong password entered", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {                //if user is already logged in then we get user details in 'currentUser' otherwise 'null'
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser:FirebaseUser? = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            Toast.makeText(baseContext, "Login Successful ", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,WelcomeActivity::class.java))
            finish()
        }
    }
}






