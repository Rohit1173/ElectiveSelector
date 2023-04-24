package com.example.electiveselector.data

data class filterSemData(
    val semNum:String,
    val electiveNum:String,
    val sub:ElectiveDetails,
    val branchList: MutableList<String>
)
