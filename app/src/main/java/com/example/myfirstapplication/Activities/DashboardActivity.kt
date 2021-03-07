package com.example.myfirstapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.myfirstapplication.Fragments.AddNoteFragment
import com.example.myfirstapplication.Fragments.LabelFragment
import com.example.myfirstapplication.Fragments.NotesFragment
import com.example.myfirstapplication.Fragments.ProfileFragment
import com.example.myfirstapplication.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_profile.*

class DashboardActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var auth: FirebaseAuth
    private  var viewIsAtHome : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        displayUserInformation(currentUser)

        customToolBarFunction()

        val navigationView : NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)    //interface which we need to implement to our class and override its method

        customHamburgerIconWithRotateAnimation()

        if(savedInstanceState == null) {                       //start dashboard activity with notes fragment already selected as default
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NotesFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_notes)
            displayView(R.id.nav_notes)
        }
    }
    //---------------------------------------------------------------------------------------------------------------
    fun displayView(viewId : Int){                                //in drawer menu options attaching fragments to corresponding menus
        when(viewId) {
            R.id.nav_notes -> {
                //placing our fragment in fragment container frame layout
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    NotesFragment()
                ).commit()
                viewIsAtHome = true
            }
            R.id.nav_profile -> {
                //placing our fragment in fragment container frame layout
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    ProfileFragment()
                ).commit()
                viewIsAtHome = false
            }
            R.id.nav_addNote -> {
                //placing our fragment in fragment container frame layout
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    AddNoteFragment()
                ).commit()
                viewIsAtHome = false
            }
            R.id.nav_label -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    LabelFragment()
                ).commit()
                viewIsAtHome = false
            }
            R.id.nav_logout -> {
                auth.signOut()
                Toast.makeText(baseContext,"Logged Out",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
    }
    //-----------------------------------------------------------------------------------------------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {   //passing displayView method to onNavigationItemSelected
        displayView(item.itemId)
        return true
    }
    //-----------------------------------------------------------------------------------------------------------------
    override fun onBackPressed() {                       //this will close the drawer if its open without directly closing app
        val navigationView : NavigationView = findViewById(R.id.nav_view)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        else if(!viewIsAtHome) {
            displayView(R.id.nav_notes)
            navigationView.setCheckedItem(R.id.nav_notes)
        }else{
            super.onBackPressed()
        }
    }
    //-----------------------------------------------------------------------------------------------------------------
     private fun displayUserInformation(currentUser: FirebaseUser?){
        val navigationView : NavigationView = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        var headerEmail : TextView = headerView.findViewById(R.id.navHeaderEmail)
        var headerImage : ImageView = headerView.findViewById(R.id.imageView)

        Glide.with(this).load(currentUser?.photoUrl).error(R.drawable.default_user_image).into(headerImage)
        headerEmail.text = currentUser?.email
    }
    //-----------------------------------------------------------------------------------------------------------------
    private fun customToolBarFunction(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    //-----------------------------------------------------------------------------------------------------------------
    private fun customHamburgerIconWithRotateAnimation(){
        drawer = findViewById(R.id.drawer_layout)
        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawer, toolbar,   //for hamburger icon
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()    //hamburger icon rotate animation when you open or close drawer
    }
    //-----------------------------------------------------------------------------------------------------------------
}