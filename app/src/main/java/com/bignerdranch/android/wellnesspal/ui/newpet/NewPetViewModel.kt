package com.bignerdranch.android.wellnesspal.ui.newpet

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.DataRepository
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.firebase.auth.FirebaseAuth

class NewPetViewModel: ViewModel() {
    private val dataRepository = DataRepository.get()
    private var auth = FirebaseAuth.getInstance()
    fun writeNewPet(name: String, color:String) {
        val pet = Pet(name,0, color, "happy", true)
        dataRepository.writePet(pet, auth.currentUser!!.uid)
    }
}