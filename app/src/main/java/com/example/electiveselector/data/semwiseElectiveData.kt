package com.example.electiveselector.data

data class semwiseElectiveData(
    val e1s1:PdfIncludedData,
    val e1s2:PdfIncludedData,
    val e1s3:PdfIncludedData,
    val choiceString1:String,
    val el1branchList:MutableList<String>,
    val e2s1:PdfIncludedData,
    val e2s2:PdfIncludedData,
    val e2s3:PdfIncludedData,
    val choiceString2:String,
    val el2branchList:MutableList<String>
)
