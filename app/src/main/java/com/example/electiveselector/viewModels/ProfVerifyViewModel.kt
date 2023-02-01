package com.example.electiveselector.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.loginData
import com.example.electiveselector.data.myResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfVerifyViewModel(application: Application):AndroidViewModel(application) {
    var profResponse: MutableLiveData<Response<myResponse>> = MutableLiveData()
     var status:MutableLiveData<String> = MutableLiveData()

    fun checkProf(userEmail: String ) {
        retrofitInstance.api.profVerify(
            userEmail
        ).enqueue(object : Callback<myResponse> {
            override fun onResponse(
                call: Call<myResponse>,
                response: Response<myResponse>
            ) {
                profResponse.value = response
                status.value="SUCCESS"
            }

            override fun onFailure(call: Call<myResponse>, t: Throwable) {
                status.value="FAIL"
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}