package com.bignerdranch.android.wellnesspal.ui.petinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.models.Pet

class PetInfoViewModel : ViewModel() {
    // LiveData object for a Pet.
    val petData = MutableLiveData<Pet>()


}