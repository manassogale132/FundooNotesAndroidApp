package com.example.myfirstapplication.Activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        loginPageToRegisterPage()
        showPasswordCheckBox()

        loginButton.setOnClickListener {
            loginUserValidation()
        }

        forgotPassword.setOnClickListener {
            forgotPassword()
        }
        val currentUser:FirebaseUser? = auth.currentUser
        updateUI(currentUser)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        sign_in_button.setOnClickListener {
            signIn()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun loginPageToRegisterPage() {
        registerUser.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            Toast.makeText(this, "Register Page Opened!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun showPasswordCheckBox() {
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                password.transformationMethod = PasswordTransformationMethod.getInstance();
            } else {
                password.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
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
            password.error = "Password must be > 6 characters"
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                    progressBar.visibility = View.VISIBLE
                } else {
                    Toast.makeText(baseContext, "Wrong email or password.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {                //if user is already logged in then we get user details in 'currentUser' otherwise 'null'
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        //val currentUser:FirebaseUser? = auth.currentUser
        //updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            Toast.makeText(baseContext, "Login Successful.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,
                DashboardActivity::class.java))
            finish()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun forgotPassword() {

        val dialogBox = AlertDialog.Builder(this)
        dialogBox.setTitle("Forgot Password")
        val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
        val username = view.findViewById<EditText>(R.id.enterEmail)
        dialogBox.setView(view)
        dialogBox.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
            forgot(username)
        })
        dialogBox.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ -> })
        dialogBox.show()
    }

    private fun forgot(username : EditText) {
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }
        if (username.text.toString().isEmpty()) {
            return
        }

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this,"Email sent!",Toast.LENGTH_SHORT).show()
                }
            }
    }
    //----------------------------------------------------------------------------------------------------------------------------------
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            }else{
                Log.w("SignInActivity",  exception.toString())
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Login Successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,
                        DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }
    //----------------------------------------------------------------------------------------------------------------------------------
}






