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
import com.example.myfirstapplication.UserData.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class LoginActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

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

       val thread : Thread = Thread(Runnable(){
           try{
               loadTestData()
           }catch(exception: Exception){
               exception.printStackTrace();
               Log.e("exceptionCheck", "onCreate: ${exception} " )
           }catch(exception: JSONException){
               exception.printStackTrace();
               Log.e("exceptionCheck", "onCreate: ${exception} " )
           }catch(exception: MalformedURLException){
               exception.printStackTrace();
               Log.e("exceptionCheck", "onCreate: ${exception} " )
           }
       })
        thread.start()
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun loadTestData() {

        val url  = URL("http://fundoonotes.incubation.bridgelabz.com/api/user/userSignUp") //Create a URL Object

        val con: HttpURLConnection = url.openConnection() as HttpURLConnection //Open a Connection

        con.setRequestMethod("POST") //Set the Request Method
        con.setRequestProperty("Content-Type", "application/json") //Set the Request Content-Type Header Parameter
        con.setRequestProperty("Accept", "*/*") //Set Response Format Type
        con.setDoOutput(true) //Ensure the Connection Will Be Used to Send Content


        val jsonInputString = """{    "firstName": "hitest",
                                      "lastName": "patil",
                                      "role": "user",
                                      "service": "advance",
                                      "password": "hitest1234",
                                      "createdDate": "2021-03-23T10:26:05.774Z",
                                      "modifiedDate": "2021-03-23T10:26:05.774Z",
                                      "email": "hitest12389@gmail.com"    }"""  //Create the Request Body


        con.outputStream.use { os ->  //Need to write it
            val input = jsonInputString.toByteArray(charset("utf-8"))
            os.write(input, 0, input.size)
        }

        //Read the Response From Input Stream
        val inputStream = try {
            con.inputStream
        }catch (exception : Exception){
            exception.printStackTrace()
            con.errorStream
        }
        BufferedReader(InputStreamReader(inputStream, "utf-8")).use { br ->
            val response = StringBuilder()
            var responseLine: String?
            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }


            val finalJSON : String = response.toString()

            Log.d("testhttps", "loadTestData: ${finalJSON} ")

            val parentObject = JSONObject(finalJSON)
            val finalObject : JSONObject = parentObject.getJSONObject("data")

            val state : Boolean = finalObject.getBoolean("success")
            val stateMessage : String = finalObject.getString("message")

            Log.d("CheckHttpResponse", "loadTestData = success : $state  message : $stateMessage")
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

                    val currentUser = auth.currentUser
                    databaseReference= database?.reference!!.child("user profiles")
                    val currentUserDb = databaseReference?.child((currentUser?.uid!!))

                    val fullname = currentUser?.displayName.toString()
                    val email = currentUser?.email.toString()

                    val users = Users(fullname, email)
                    currentUserDb?.setValue(users)

                    Toast.makeText(baseContext, "Login Successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,
                        DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }
    //----------------------------------------------------------------------------------------------------------------------------------
}






