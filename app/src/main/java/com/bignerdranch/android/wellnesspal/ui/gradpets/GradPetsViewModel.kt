package com.bignerdranch.android.wellnesspal.ui.gradpets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.models.Pet
import com.bignerdranch.android.wellnesspal.models.UserLog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
private const val TAG = "GradPetsViewModel"
class GradPetsViewModel : ViewModel() {

    val gradPetsData =  MutableLiveData<List<Pet>>()

    val gradPets = mutableListOf<Pet>()
    lateinit var GradPetListener: ValueEventListener

    fun addGradPetEventListener(petsQuery: Query){
        GradPetListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                gradPets.clear()
                for (pet in dataSnapshot.children){
                    val pet = pet.getValue<Pet>()
                    pet?.let{
                        gradPets.add(it)
                    }
                }
                Log.d(TAG, gradPets.toString())
                gradPetsData.value = gradPets
            }
            override fun onCancelled(error: DatabaseError) {
                //write a log message if a read failed
                Log.d(TAG, "loadGradPetsRecyclerView: onCancelled", error.toException())
            }

        }
        petsQuery.addValueEventListener(GradPetListener)
    }



}