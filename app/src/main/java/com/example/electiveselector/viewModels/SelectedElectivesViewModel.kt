package com.example.electiveselector.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.selectedElectivesData
import com.example.electiveselector.data.semData
import com.example.electiveselector.data.semResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectedElectivesViewModel(application: Application): AndroidViewModel(application){

    var selectedElectivesResponse: MutableLiveData<Response<selectedElectivesData>> = MutableLiveData()

    fun getSelectedElectives(userEmail: String) {
        retrofitInstance.api.getSelectedElectives(
            userEmail
        ).enqueue(object : Callback<selectedElectivesData> {
            override fun onResponse(
                call: Call<selectedElectivesData>,
                response: Response<selectedElectivesData>
            ) {
                selectedElectivesResponse.value = response
            }

            override fun onFailure(call: Call<selectedElectivesData>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}