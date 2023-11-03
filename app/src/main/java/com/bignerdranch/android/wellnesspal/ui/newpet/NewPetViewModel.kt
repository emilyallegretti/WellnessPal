package com.bignerdranch.android.wellnesspal.ui.newpet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewPetViewModel: ViewModel() {
    private val dataRepository = DataRepository.get()
    private var auth = FirebaseAuth.getInstance()
    fun writeNewPet(name: String, color:String) {
        val pet = Pet(name,0, color, "happy", true)
       // return viewModelScope.async(Dispatchers.IO) {
        dataRepository.writePet(pet, auth.currentUser!!.uid)
      //  }.await()
    }
}