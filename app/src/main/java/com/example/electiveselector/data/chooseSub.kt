package com.example.electiveselector.data

data class chooseSub(
    val userEmail:String,
    val userName:String,
    val semNum:String,
    val electiveNum:String,
    val choiceString:String,
    val branchList:MutableList<String>
)
