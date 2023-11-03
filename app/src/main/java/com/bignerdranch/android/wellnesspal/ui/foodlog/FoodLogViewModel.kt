package com.bignerdranch.android.wellnesspal.ui.foodlog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.EatLog
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "FoodLogViewModel"
class FoodLogViewModel : ViewModel(){

    private val dataRepository = DataRepository.get()
    private val auth = FirebaseAuth.getInstance()
     val totalLogsCurrDateData = MutableLiveData<Int>()
    lateinit var currPetListener : ValueEventListener
    lateinit var logsListener: ValueEventListener
    lateinit var goalListener: ValueEventListener
     val goalData = MutableLiveData<String>()
    lateinit var currPet: Pet
    lateinit var currPetKey: String
    var goalMet : Boolean = false


    private val _text = MutableLiveData<String>().apply {
        value = "Enter the number of meals eaten"
    }
    val text: LiveData<String> = _text

    private fun getDateAndTime(): String {
        val simpleDate = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return simpleDate.format(Date())
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun writeNewFoodLog(meals:String) {
        val date = getDateAndTime()
        val foodLog = EatLog("eat", date, meals )
        viewModelScope.launch {
            dataRepository.writeNewEatLog(foodLog, auth!!.currentUser!!.uid)
        }
    }
    fun incrementPetAge() {
        dataRepository.incrementPetAge(auth.currentUser!!.uid, currPet, currPetKey)
    }

    fun addPetEventListener(ref: Query) {
        currPetListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currPet = snapshot.children.first().getValue<Pet>()!!
                currPetKey = snapshot.children.first().key.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.d(TAG, "oncancelled:currPet")
            }
        }
        ref.addValueEventListener(currPetListener)
    }
    fun addGoalEventListener(ref: DatabaseReference) {
        goalListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val goal = snapshot.getValue<String>()
                Log.d(TAG, goal.toString())
                goal?.let{
                    goalData.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG,"getFoodGoal:onCancelled", error.toException())
            }
        }
        ref.addValueEventListener(goalListener)
    }

// this listener will update the total food logs entered on the current date
    fun addTotalLogsEventListener(query: Query) {
        var totalMeals = 0
        logsListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null) {
                    for (child in snapshot.children) {
                        // each child of snapshot will be a log with type 'eat'
                        // find all logs with a date equal to the current date and add up num meals
                        val log = child.getValue<EatLog>()
                        val currDate = getCurrentDate()
                        if (log != null) {
                            if (log.date?.contains(currDate) == true) {
                                val numMeals = log.numMeals.toString()
                                Log.d(TAG, "number of meals: $numMeals")
                                totalMeals += log.numMeals?.toInt() ?: 0
                            }

                        }
                    }
                    totalLogsCurrDateData.value = totalMeals
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    query.addValueEventListener(logsListener)
    }
}