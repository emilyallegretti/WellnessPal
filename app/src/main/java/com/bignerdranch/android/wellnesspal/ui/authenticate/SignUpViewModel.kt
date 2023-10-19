package com.bignerdranch.android.wellnesspal.ui.authenticate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel : ViewModel() {
    private val dataRepository = DataRepository.get()
    private lateinit var auth : FirebaseAuth

        fun writeNewUser(email:String, password:String, uid: String) {
            val user = User(email, password)
            dataRepository.writeNewUser(user, uid)
        }
}
