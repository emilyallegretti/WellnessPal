package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.wellnesspal.MainActivity
import com.bignerdranch.android.wellnesspal.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        // check if user is signed in--if so, start MainActivity.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // check the amount of time since currentUser's last sign-in; if it has been more than a day, keep them
            // signed out
            //86400000 milliseconds in one day
            if (Date().time!! - currentUser.metadata!!.lastSignInTimestamp!!  > 86400000) {
                auth.signOut()
                Toast.makeText(this, "You've been signed out because more than a day has passed since your last signin", Toast.LENGTH_SHORT )
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}