package com.example.electiveselector.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SemDataViewModel(application: Application): AndroidViewModel(application) {

    var semDataResponse: MutableLiveData<Response<StudentsDataModel>> = MutableLiveData()

    fun getSemData(filterSemData: filterSemData) {
        retrofitInstance.api.getSemData(
            filterSemData
        ).enqueue(object : Callback<StudentsDataModel> {
            override fun onResponse(
                call: Call<StudentsDataModel>,
                response: Response<StudentsDataModel>
            ) {
                semDataResponse.value = response
            }

            override fun onFailure(call: Call<StudentsDataModel>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    var subjectsResponse: MutableLiveData<Response<SubjectsDataModel>> = MutableLiveData()

    fun getSubjectsData(semAndBranchList: SemAndBranchList) {
        retrofitInstance.api.getSubjectsData(
            semAndBranchList.semNum,
            semAndBranchList.electiveNum,
            semAndBranchList.branchList
        ).enqueue(object : Callback<SubjectsDataModel> {
            override fun onResponse(
                call: Call<SubjectsDataModel>,
                response: Response<SubjectsDataModel>
            ) {
                subjectsResponse.value = response
            }

            override fun onFailure(call: Call<SubjectsDataModel>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    var branchesResponse: MutableLiveData<Response<BranchesDataModel>> = MutableLiveData()

    fun getBranchesData(semAndElectiveData: SemAndElectiveData) {
        retrofitInstance.api.getBranchesData(
            semAndElectiveData.semNum,
            semAndElectiveData.electiveNum,
        ).enqueue(object : Callback<BranchesDataModel> {
            override fun onResponse(
                call: Call<BranchesDataModel>,
                response: Response<BranchesDataModel>
            ) {
                branchesResponse.value = response
            }

            override fun onFailure(call: Call<BranchesDataModel>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }



}