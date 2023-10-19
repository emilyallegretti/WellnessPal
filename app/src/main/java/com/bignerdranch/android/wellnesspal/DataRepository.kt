package com.bignerdranch.android.wellnesspal

import android.content.Context
import android.util.Log
import com.bignerdranch.android.wellnesspal.models.Goal
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

private const val TAG = "DataRepository"
// This class performs all CRUD operations on the remote database, following Android's suggested repository pattern.
class DataRepository private constructor(context: Context) {

    // Get a reference to the Firebase Realtime Database for this project.
    private val database = Firebase.database.reference
    private val userAuth = Firebase.auth.currentUser


    // QUERIES


    // Write a new User.
    fun writeUser(user: User, uid: String) {
        Log.d(TAG, "writing new user... ")
        database.child("users").child(uid).setValue(user)
    }
    fun writeGoal(goal: Goal, uid:String) {
        Log.d(TAG, "writing new goal at " + uid)
        database.child("users").child(uid).child("goal").setValue(goal)
    }

    fun updatePassword(uid: String, newPassword: String){
        userAuth!!.updatePassword(newPassword)
        //TODO add on completion listeners
    }

    companion object {
        private var INSTANCE: DataRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DataRepository(context)
            }
        }

        fun get(): DataRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}