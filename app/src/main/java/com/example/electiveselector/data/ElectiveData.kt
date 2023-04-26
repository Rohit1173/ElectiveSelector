package com.example.electiveselector.data

data class ElectiveData(
    val semNum:String,
    val electiveNum:String,
    val sub1: PdfIncludedData,
    val sub2: PdfIncludedData,
    val sub3: PdfIncludedData,
    val branchList:MutableList<String>,
    val addedBy:String,
    val addedTime:String,
)
