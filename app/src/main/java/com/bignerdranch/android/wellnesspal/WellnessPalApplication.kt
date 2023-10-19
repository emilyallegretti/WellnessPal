package com.bignerdranch.android.wellnesspal

import android.app.Application

class WellnessPalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataRepository.initialize(this)
    }
}