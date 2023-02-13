package com.example.electiveselector.data

import com.example.electiveselector.data.ElectiveData

data class AnnouncementResponse(
    val status:String,
    val message: MutableList<ElectiveData>
)
