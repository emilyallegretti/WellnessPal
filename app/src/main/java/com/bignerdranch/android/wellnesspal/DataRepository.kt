package com.bignerdranch.android.wellnesspal

import android.content.Context
import android.util.Log
import com.bignerdranch.android.wellnesspal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "DataRepository"
// This class performs all CRUD operations on the remote database, following Android's suggested repository pattern.
class DataRepository private constructor(context: Context) {

    // Get a reference to the Firebase Realtime Database for this project.
    private val database = Firebase.database.reference

    // QUERIES

    // Write a new User.
    fun writeNewUser(user: User, uid: String) {
        Log.d(TAG, "writing new user...")
        database.child("users").child(uid).setValue(user)
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