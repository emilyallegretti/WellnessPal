package com.bignerdranch.android.wellnesspal

import android.content.Context
import android.util.Log
import com.bignerdranch.android.wellnesspal.models.EatLog
import com.bignerdranch.android.wellnesspal.models.Goal
import com.bignerdranch.android.wellnesspal.models.Pet
import com.bignerdranch.android.wellnesspal.models.SleepLog
import com.bignerdranch.android.wellnesspal.models.User
import com.bignerdranch.android.wellnesspal.models.UserLog
import com.bignerdranch.android.wellnesspal.models.WaterLog

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
    // update the pet's emotion to given string.
    fun setMood(uid: String, mood: String, currPetKey: String) {
        database.child("users/$uid/pets/$currPetKey/emotion").setValue(mood).addOnSuccessListener {
            Log.d(TAG, "successfully set mood to $mood")
        }.addOnFailureListener {
            Log.d(TAG, "failed to update mood", it)
        }
    }

    // update pet's 'current' attribute to false.
    fun updateCurrentFlag(uid: String, currPetKey: String) {
        // get current pet under current user
        database.child("users/$uid/pets/$currPetKey/current").setValue(false).addOnSuccessListener {
            Log.d(TAG, "successfully set current to false")
        }.addOnFailureListener {
            Log.d(TAG, "failed to update current flag", it)
        }
    }
    // increment Pet age.
    fun incrementPetAge(uid: String, currPet: Pet , currPetKey: String) {
        val currAge = currPet.age!!.toInt()
        val newAge = currAge + 1
        Log.d(TAG, "currAge: $currAge")

        database.child("users/$uid/pets/$currPetKey/age").setValue(newAge).addOnSuccessListener {
                Log.d(TAG, "pet age successfully incremented")
            }.addOnFailureListener {
                Log.d(TAG, "PET AGE INCREMENT FAILED")
            }


    }

    // Write a new Pet at the given uid.
    fun writePet(pet: Pet, uid: String): Boolean {
        var success=false
        Log.d(TAG, "writing new pet at $uid")
        val key = database.child("users/$uid/pets").push()
        // set new Pet value at the given key
        key.setValue(pet).addOnSuccessListener {
            Log.d(TAG, "new Pet successfully written")
            success=true
        }.addOnFailureListener {
            Log.d(TAG, "failure writing new pet at $uid", it)
        }
        return success
    }

    // Write a new User.
    fun writeUser(user: User, uid: String) {
        Log.d(TAG, "writing new user... ")
        database.child("users").child(uid).setValue(user)

        getLogCount(database, uid)
    }

    fun writeNewEatLog(log: EatLog, uid: String) {
        //outputs appear in logcat to show log info
        Log.d(TAG, "writing new log at $uid and log count $logCount")
        Log.d(TAG, "log is $log")

        //push creates the key for the new log in the db
        //logRef holds the db reference to that key
        val logRef = database.child("users").child(uid).child("logs").push()

        //set the value at the key to the values in log (EatLog object)
        logRef.setValue(log).addOnSuccessListener {
            Log.d(TAG, "successfully wrote log")
        }.addOnFailureListener{
            Log.d(TAG, "failure writing log")
        }
    }

    fun writeNewWaterLog(log: WaterLog, uid: String) {
        //outputs appear in logcat to show log info
        Log.d(TAG, "writing new log at $uid and log count $logCount")
        Log.d(TAG, "log is $log")

        //push creates the key for the new log in the db
        //logRef holds the db reference to that key
        val logRef = database.child("users").child(uid).child("logs").push()

        //set the value at the key to the values in log (WaterLog object)
        logRef.setValue(log).addOnSuccessListener {
            Log.d(TAG, "successfully wrote log")
        }.addOnFailureListener{
            Log.d(TAG, "failure writing log")
        }
    }

    fun writeNewSleepLog(log: SleepLog, uid: String) {
        //outputs appear in logcat to show log info
        Log.d(TAG, "writing new log at $uid and log count $logCount")
        Log.d(TAG, "log is $log")

        //push creates the key for the new log in the db
        //logRef holds the db reference to that key
        val logRef = database.child("users").child(uid).child("logs").push()

        //set the value at the key to the values in log (SleepLog object)
        logRef.setValue(log).addOnSuccessListener {
            Log.d(TAG, "successfully wrote log")
        }.addOnFailureListener{
            Log.d(TAG, "failure writing log")
        }
    }

    // Delete a user.
    fun deleteUser(uid: String) {
        Log.d(TAG, "deleting user from database")
        database.child("users").child(uid).removeValue().addOnSuccessListener {
            Log.d(TAG, "successfully removed user from database")
        }.addOnFailureListener{
            Log.e(TAG, "error removing from database", it)
        }
    }
    fun writeGoal(goal: Goal, uid:String) {
        Log.d(TAG, "writing new goal at $uid")
        database.child("users/$uid/goal").setValue(goal).addOnSuccessListener {
            Log.d(TAG, "successfully wrote goal")
        }.addOnFailureListener{
            Log.d(TAG, "failure writing goal")
        }
    }

    fun updateGoal(newVal:String, goalType: String, uid: String) {
        Log.d(TAG, "updating $goalType goal")
        val targetChild = goalType + "Goal"     // create child node name to be updated
        database.child("users/$uid/goal/$targetChild").setValue(newVal).addOnSuccessListener {
            Log.d(TAG, "successfully updated goal")
        }.addOnFailureListener{
            Log.d(TAG, "Failed to update goal")
        }
    }

    fun updateBirthday(uid: String, currPetKey :String, birthdayNum :String ) {
        Log.d(TAG, "updating birthday$birthdayNum")
        database.child("users/$uid/pets/$currPetKey/birthday$birthdayNum").setValue(true).addOnSuccessListener {
            Log.d(TAG, "successfully updated birthday$birthdayNum")
        }.addOnFailureListener{
            Log.d(TAG, "Failed to update birthday$birthdayNum")
        }
    }



//    fun updatePassword(uid: String, newPassword: String){
//        userAuth!!.updatePassword(newPassword)
//        //TODO add on completion listeners
//    }

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