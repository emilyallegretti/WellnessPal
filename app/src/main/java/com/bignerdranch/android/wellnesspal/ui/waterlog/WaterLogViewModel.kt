package com.bignerdranch.android.wellnesspal.ui.waterlog

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.LogOld
import com.bignerdranch.android.wellnesspal.models.WaterLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class WaterLogViewModel : ViewModel(){

    private val dataRepository = DataRepository.get()
    private var auth = FirebaseAuth.getInstance()
    //private val database = Firebase.database.reference
    //private val uid = auth.currentUser!!.uid


//    private val _text = MutableLiveData<String>().apply {
//        value = "Enter the number of meals eaten"
//    }
    //val text: LiveData<String> = _text

    private fun getDate(): String {
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return simpleDate.format(Date())
    }

    fun writeNewWaterLog(ounces :String) {
        val date = getDate()
        val waterLog = WaterLog("water", date, ounces )
        dataRepository.writeNewWaterLog(waterLog, auth!!.currentUser!!.uid)
    }
}