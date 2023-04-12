package com.example.electiveselector.data

data class ElectiveData(
    val semNum:String,
    val electiveNum:String,
    val sub1: ElectiveDetails,
    val sub2: ElectiveDetails,
    val sub3: ElectiveDetails,
    val branchList:MutableList<String>,
    val addedBy:String,
    val addedTime:String,
)
