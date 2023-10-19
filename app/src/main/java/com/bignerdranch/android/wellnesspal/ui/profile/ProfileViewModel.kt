package com.bignerdranch.android.wellnesspal.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ProfileViewModel : ViewModel() {
    private val dataRepository = DataRepository.get()
    private var auth = FirebaseAuth.getInstance()

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData



    fun addUserEventListener(userReference: DatabaseReference){
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user = dataSnapshot.getValue<User>()

                //update the liveData with the new value from the listener
                user?.let {
                    _userData.value = it
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        userReference.addValueEventListener(userListener)

    }

    /*
    Used when a user tries to reset their password
    Compares their current password in the db
        with the value they entered as their old password
    If the passwords match, return true
    If the passwords don't match, return false
     */
    private fun passwordCompare(enteredPassword: String): Boolean{
        if (_userData.value!!.hashedPass == enteredPassword) {
            return true
        }
        return false
    }

    fun resetPassword(oldPassword: String, newPassword: String, reEnteredNewPass: String){
        if (passwordCompare(oldPassword)){
            dataRepository.updatePassword(auth.currentUser!!.uid, newPassword)
        }
    }

}