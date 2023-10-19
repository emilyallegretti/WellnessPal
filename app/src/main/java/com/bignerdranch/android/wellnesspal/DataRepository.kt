package com.bignerdranch.android.wellnesspal

import android.content.Context
import android.util.Log
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "DataRepository"
// This class performs all CRUD operations on the remote database, following Android's suggested repository pattern.
class DataRepository private constructor(context: Context) {

    // Get a reference to the Firebase Realtime Database for this project.
    private val database = Firebase.database.reference

    // Storing the number of logs for the user.
    private var logCount = 0

    // Getting the number of logs in the user database.
    private fun getLogCount(mDatabase: DatabaseReference, userId: String ) {
        mDatabase.child("users").child(userId).child("logs").get().addOnSuccessListener {
            Log.i("firebase", "Got logCount ${it.childrenCount}")
            logCount = it.childrenCount.toInt()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting logCount", it)
        }
    }

    // QUERIES

    // Write a new User.
    fun writeNewUser(user: User, uid: String) {
        Log.d(TAG, "writing new user...")
        database.child("users").child(uid).setValue(user)

        getLogCount(database, uid)
    }

    // Write a new Log.
    fun writeNewLog(log: com.bignerdranch.android.wellnesspal.models.Log) {
        logCount++
        database.child("logs").child(logCount.toString()).setValue(log)
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