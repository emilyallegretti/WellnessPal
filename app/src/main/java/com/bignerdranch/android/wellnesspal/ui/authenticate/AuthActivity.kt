package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.wellnesspal.MainActivity
import com.bignerdranch.android.wellnesspal.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth

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
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}