package com.bignerdranch.android.wellnesspal.ui.foodlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.EatLog
import com.bignerdranch.android.wellnesspal.models.Log
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date


class FoodLogViewModel : ViewModel(){

    private val dataRepository = DataRepository.get()
    private val auth = FirebaseAuth.getInstance()

    private val _text = MutableLiveData<String>().apply {
        value = "Enter the number of meals eaten"
    }
    val text: LiveData<String> = _text

    private fun getDate(): String {
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return simpleDate.format(Date())
    }

    fun writeNewFoodLog(meals:String) {
        val date = getDate()
        val foodLog = EatLog("eat", date, meals )

        dataRepository.writeNewEatLog(foodLog, auth!!.currentUser!!.uid )
    }
}