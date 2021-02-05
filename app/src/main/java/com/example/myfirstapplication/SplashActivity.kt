package com.example.myfirstapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity  : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        navigateToLoginAfterDelay()
        animateAppTextName()
    }

    private fun navigateToLoginAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }

    private fun animateAppTextName() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        textview1.startAnimation(animation)
    }
}