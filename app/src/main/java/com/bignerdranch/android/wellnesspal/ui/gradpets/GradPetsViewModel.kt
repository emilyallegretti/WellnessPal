package com.bignerdranch.android.wellnesspal.ui.gradpets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GradPetsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is GradPets Fragment"
    }
    val text: LiveData<String> = _text


}