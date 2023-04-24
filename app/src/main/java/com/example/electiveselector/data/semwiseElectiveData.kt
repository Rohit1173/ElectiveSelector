package com.example.electiveselector.data

data class semwiseElectiveData(
    val e1s1:ElectiveDetails,
    val e1s2:ElectiveDetails,
    val e1s3:ElectiveDetails,
    val choiceString1:String,
    val el1branchList:MutableList<String>,
    val e2s1:ElectiveDetails,
    val e2s2:ElectiveDetails,
    val e2s3:ElectiveDetails,
    val choiceString2:String,
    val el2branchList:MutableList<String>
)
