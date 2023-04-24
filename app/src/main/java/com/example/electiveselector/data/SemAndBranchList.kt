package com.example.electiveselector.data

data class SemAndBranchList(
    val semNum:String,
    val electiveNum:String,
    val branchList: MutableList<String>
)