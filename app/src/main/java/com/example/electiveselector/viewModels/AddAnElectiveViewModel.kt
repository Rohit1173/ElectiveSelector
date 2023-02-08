package com.example.electiveselector.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.myResponse
import com.example.electiveselector.fragments.ElectiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAnElectiveViewModel(application: Application):AndroidViewModel(application) {
    var addElectiveResponse: MutableLiveData<Response<myResponse>> = MutableLiveData()


    fun addElective(electiveData: ElectiveData) {
        retrofitInstance.api.addElectiveDetails(
            electiveData
        ).enqueue(object : Callback<myResponse> {
            override fun onResponse(
                call: Call<myResponse>,
                response: Response<myResponse>
            ) {
                addElectiveResponse.value = response
            }

            override fun onFailure(call: Call<myResponse>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}