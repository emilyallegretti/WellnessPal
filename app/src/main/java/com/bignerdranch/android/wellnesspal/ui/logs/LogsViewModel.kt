package com.bignerdranch.android.wellnesspal.ui.logs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.models.EatLog
import com.bignerdranch.android.wellnesspal.models.SleepLog
import com.bignerdranch.android.wellnesspal.models.User
import com.bignerdranch.android.wellnesspal.models.UserLog
import com.bignerdranch.android.wellnesspal.models.WaterLog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
private const val TAG = "LogsViewModel"
class LogsViewModel : ViewModel() {

    val logData = MutableLiveData<List<UserLog>>()
    val logs = mutableListOf<UserLog>()
    lateinit var LogListener: ValueEventListener
//    lateinit var eatLogListener: ValueEventListener
//    lateinit var waterLogListener: ValueEventListener

    //val logData = MutableLiveData<Log>()
//    val eatLogData = MutableLiveData<EatLog>()
//    val waterLogData = MutableLiveData<WaterLog>()

    fun addLogEventListener(logReference: DatabaseReference){
        LogListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){

//                val userLog = dataSnapshot.getValue<Log>()
//                Log.d(TAG, userLog.toString())

                for (logSnapshot in dataSnapshot.children){
                    val log = logSnapshot.getValue<UserLog>()
                    //Log.d(TAG, log.toString())
                    log?.let{
                        logs.add(it)
                    }
                }
                logData.value = logs

                //update the liveData with the new value from the listener
//                userLog?.let {
//                    logData.value = it
//                }
            }
            override fun onCancelled(error: DatabaseError) {
                //write a log message if a read failed
                Log.d(TAG, "loadUser: onCancelled", error.toException())
            }

        }
        logReference.addValueEventListener(LogListener)
    }



}