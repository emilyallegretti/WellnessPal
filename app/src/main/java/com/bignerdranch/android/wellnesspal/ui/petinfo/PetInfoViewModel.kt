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
import java.util.Locale

private const val TAG= "PetInfoViewModel"
class PetInfoViewModel : ViewModel() {
    // LiveData object for a Pet.
    val petData = MutableLiveData<Pet>()
    val dataRepository = DataRepository.get()
    val auth: FirebaseAuth =FirebaseAuth.getInstance()
    lateinit var logsListener: ValueEventListener
    var mostRecentLogDate = MutableLiveData<Date>()

    fun updateCurrentFlag(currPetKey: String) {
        dataRepository.updateCurrentFlag(auth.currentUser!!.uid, currPetKey)
    }
    private fun stringToDate(str:String): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        Log.d(TAG, "input to stringtodate: $str")
        val ret = dateFormat.parse(str)
        Log.d(TAG, "stringtodate: $ret")
        return dateFormat.parse(str) ?: Date()
    }
      fun getCurrentDate(): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val currentDateString = dateFormat.format(Date())
         Log.d(TAG, "$currentDateString")
        return dateFormat.parse(currentDateString)
    }
    // get the difference in days between two Dates.
    fun getDateDiff(): Long {
        val currDate = getCurrentDate()
        Log.d(TAG, "currdate: $currDate")
        val diff = currDate?.time!! - mostRecentLogDate.value?.time!!
        return ((diff/ 1_000 /60)/60)/24
    }
    // updates pet's mood in the database to given emotion.
    fun setMood(mood: String, currPetKey: String) {
        if (currPetKey != "") {
            dataRepository.setMood(auth.currentUser!!.uid, mood, currPetKey)
        }
    }

    fun addMostRecentLogEventListener(logReference: Query) {
        Log.d(TAG, "addMostRecentLogEventListener")
        logsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val log = snapshot.children.first().child("date").value as String
                mostRecentLogDate.value = stringToDate(log.split(" ")[0])
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

//        logsListener = object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val newLogDate = snapshot.child("date").value as String
//                Log.d(TAG,"new log date: $newLogDate")
//                mostRecentLogDate.value = stringToDate(newLogDate.split(" ")[0])
//                Log.d(TAG, "mostRecentLogDate updated to ${mostRecentLogDate.value}")
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
        logReference.addValueEventListener(logsListener)
    }
}