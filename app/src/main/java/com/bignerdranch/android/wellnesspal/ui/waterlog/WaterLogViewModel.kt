package com.bignerdranch.android.wellnesspal.ui.waterlog

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.UserLog
import com.bignerdranch.android.wellnesspal.models.WaterLog
import com.google.firebase.auth.FirebaseAuth
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
        val waterLog = UserLog("water", date, null, null, ounces)
        dataRepository.writeNewWaterLog(waterLog, auth!!.currentUser!!.uid)
    }
}