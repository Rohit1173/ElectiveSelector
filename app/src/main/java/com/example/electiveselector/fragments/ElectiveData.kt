package com.example.electiveselector.fragments

data class ElectiveData(
    val semNum:String,
    val electiveNum:String,
    val sub1:ElectiveDetails,
    val sub2:ElectiveDetails,
    val sub3:ElectiveDetails,
    val addedBy:String,
    val addedTime:String,
    val scheduledAt:String
)
