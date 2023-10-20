package com.bignerdranch.android.wellnesspal.models

import android.provider.ContactsContract.CommonDataKinds.Email

data class SleepLog(val type: String?=null, val date:String?=null, val hoursSlept:String?=null){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to type,
            "date" to date,
            "hoursSlept" to hoursSlept
        )
    }
}
