package com.bignerdranch.android.wellnesspal.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.Pet
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
private const val TAG = "ProfileViewModel"

class ProfileViewModel : ViewModel() {
    private val dataRepository = DataRepository.get()
    private var auth = FirebaseAuth.getInstance()
    lateinit var userListener: ValueEventListener

    val userData = MutableLiveData<User>()
    val gradPetsCountData = MutableLiveData<Int>()
    lateinit var gradPetCountListener: ValueEventListener

    fun addGradPetCountEventListener(petsQuery: Query) {
        gradPetCountListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var count = 0
                for (pet in dataSnapshot.children) {
                    count++
                }
                gradPetsCountData.value=count

            }

            override fun onCancelled(error: DatabaseError) {
                //write a log message if a read failed
                Log.d(TAG,
                    "loadGradPetsRecyclerView: onCancelled",
                    error.toException()
                )
            }

        }
        petsQuery.addValueEventListener(gradPetCountListener)
    }

    /*
    Add event listener to the current user in the database
    Add data read into live data
     */

// todo: move all queries to dataRepository

    fun addUserEventListener(userReference: DatabaseReference){
        userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user = dataSnapshot.getValue<User>()
                //Log.d(TAG, user.toString())

                //update the liveData with the new value from the listener
                user?.let {
                    userData.value = it
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //write a log message if a read failed
                Log.d(TAG, "loadUser: onCancelled", error.toException())
            }

        }
        userReference.addValueEventListener(userListener)
    }

    // delete the user's entry in the database
    fun deleteUserEntry() {
        dataRepository.deleteUser(auth.currentUser!!.uid)
    }

    fun updateGoal(newVal:String, goalType: String) {
        dataRepository.updateGoal(newVal, goalType, auth.currentUser!!.uid)
    }

}