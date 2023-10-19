package com.bignerdranch.android.wellnesspal.models

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(val email: String?=null, val password:String?=null) {
}