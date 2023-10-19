package com.bignerdranch.android.wellnesspal.ui.authenticate

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.Goal
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    private val dataRepository = DataRepository.get()
    private var auth = FirebaseAuth.getInstance()

    // maybe with a conditional check on the email we can create AND update users in this method, same with rest of methods
        fun writeUser(email:String, password:String, fname:String, lname:String) {
            val user = User(email, password, fname, lname)
            dataRepository.writeUser(user, auth!!.currentUser.toString())
        }

    fun writeGoal(waterGoal: String, eatGoal:String, sleepGoal: String) {
        val goal = Goal(waterGoal, eatGoal, sleepGoal)
        dataRepository.writeGoal(goal, auth!!.currentUser.toString())
    }
}
