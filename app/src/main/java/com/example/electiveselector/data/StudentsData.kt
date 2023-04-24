package com.example.electiveselector.data

data class StudentsData(
    val userName:String,
    val userEmail:String,
    val sub:ElectiveDetails,
    val semNum:String,
    val electiveNum:String,
    val branchList:MutableList<String>
)
