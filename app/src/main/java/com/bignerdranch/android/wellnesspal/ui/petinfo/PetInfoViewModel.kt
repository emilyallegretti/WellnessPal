package com.bignerdranch.android.wellnesspal.ui.petinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG= "PetInfoViewModel"
class PetInfoViewModel : ViewModel() {
    // LiveData object for a Pet.
    val petData = MutableLiveData<Pet>()
    val dataRepository = DataRepository.get()
    val auth: FirebaseAuth =FirebaseAuth.getInstance()
    lateinit var logsListener: ChildEventListener
    lateinit var mostRecentLogDate: Date

    fun updateCurrentFlag(currPetKey: String) {
        dataRepository.updateCurrentFlag(auth.currentUser!!.uid, currPetKey)
    }
    private fun stringToDate(str:String): Date {
        val pattern = "dd/MM/yyyy"
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.parse(str) ?: Date()
    }
     fun getCurrentDate(): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = Date()
        return dateFormat.parse(currentDate.toString()) ?: currentDate
    }
    // updates pet's mood in the database to given emotion.
    fun setMood(mood: String, currPetKey: String) {
        dataRepository.setMood(auth.currentUser!!.uid, mood, currPetKey)
    }
    fun addMostRecentLogEventListener(logReference: DatabaseReference) {
        logsListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newLogDate : String = snapshot.child("date").value as String
                mostRecentLogDate = stringToDate(newLogDate)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    }

//
}