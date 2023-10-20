package com.bignerdranch.android.wellnesspal.ui.sleeplog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.Log
import com.bignerdranch.android.wellnesspal.models.SleepLog
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

class SleepLogViewModel : ViewModel(){

    private val dataRepository = DataRepository.get()
    private val auth = FirebaseAuth.getInstance()

    private val _text = MutableLiveData<String>().apply {
        value = "Enter the number of hours slept"
    }
    val text: LiveData<String> = _text

    private fun getDate(): String {
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return simpleDate.format(Date())
    }

    fun writeNewSleepLog(hours:String) {
        val date = getDate()
        val sleepLog = SleepLog("sleep", date, hours )

        dataRepository.writeNewSleepLog(sleepLog, auth!!.currentUser!!.uid)
    }
}