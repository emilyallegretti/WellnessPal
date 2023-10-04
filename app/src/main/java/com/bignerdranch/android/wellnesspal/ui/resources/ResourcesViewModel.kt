package com.bignerdranch.android.wellnesspal.ui.resources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResourcesViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Resources Fragment"
    }
    val text: LiveData<String> = _text
}