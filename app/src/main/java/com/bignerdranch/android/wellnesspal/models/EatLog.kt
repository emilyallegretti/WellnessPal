package com.bignerdranch.android.wellnesspal.models

import android.provider.ContactsContract.CommonDataKinds.Email

data class EatLog(val type: String?=null,  val date:String?=null , val numMeals:String?=null ){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to type,
            "date" to date,
            "numMeals" to numMeals
        )
    }
}
