package com.example.electiveselector.fragments

import android.os.Message

data class AnnouncementResponse(
    val status:String,
    val message: MutableList<ElectiveData>
)
