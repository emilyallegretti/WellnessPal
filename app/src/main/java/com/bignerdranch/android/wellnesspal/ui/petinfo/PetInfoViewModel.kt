package com.bignerdranch.android.wellnesspal.ui.petinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PetInfoViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Pet Fragment"
    }
    val text: LiveData<String> = _text
}