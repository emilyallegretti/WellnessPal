package com.bignerdranch.android.wellnesspal.ui.authenticate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.User

class SignUpViewModel : ViewModel() {
    private val dataRepository = DataRepository.get()

        fun writeNewUser(email:String, password:String) {
            val user = User(email, password)
            dataRepository.writeNewUser(user)
        }
}
