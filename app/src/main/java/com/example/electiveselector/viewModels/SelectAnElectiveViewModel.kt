package com.example.electiveselector.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.chooseSub
import com.example.electiveselector.data.myResponse
import com.example.electiveselector.data.semData
import com.example.electiveselector.data.semResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectAnElectiveViewModel(application: Application):AndroidViewModel(application) {
    var selectAnElectiveResponse: MutableLiveData<Response<semResponse>> = MutableLiveData()

    fun getSemWiseElectiveData(semData: semData) {
        retrofitInstance.api.semData(
            semData.semNum,
            semData.userEmail
        ).enqueue(object : Callback<semResponse> {
            override fun onResponse(
                call: Call<semResponse>,
                response: Response<semResponse>
            ) {
                selectAnElectiveResponse.value = response
            }

            override fun onFailure(call: Call<semResponse>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    var chooseSubResponse: MutableLiveData<Response<myResponse>> = MutableLiveData()

    fun chooseASubject(chooseSub: chooseSub) {
        retrofitInstance.api.chooseSub(
            chooseSub.userEmail,
            chooseSub.userName,
            chooseSub.semNum,
            chooseSub.electiveNum,
            chooseSub.choiceString,
            chooseSub.branchList
        ).enqueue(object : Callback<myResponse> {
            override fun onResponse(
                call: Call<myResponse>,
                response: Response<myResponse>
            ) {
                chooseSubResponse.value = response
            }

            override fun onFailure(call: Call<myResponse>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}