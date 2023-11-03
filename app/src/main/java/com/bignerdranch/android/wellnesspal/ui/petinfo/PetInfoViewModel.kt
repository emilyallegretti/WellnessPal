package com.bignerdranch.android.wellnesspal.ui.petinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
private const val TAG= "PetInfoViewModel"
class PetInfoViewModel : ViewModel() {
    // LiveData object for a Pet.
    val petData = MutableLiveData<Pet>()
    val dataRepository = DataRepository.get()
    val auth: FirebaseAuth =FirebaseAuth.getInstance()

    fun updateCurrentFlag(currPetKey: String) {
        dataRepository.updateCurrentFlag(auth.currentUser!!.uid, currPetKey)
    }

//
}