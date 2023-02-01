package com.example.electiveselector.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.myResponse
import com.example.electiveselector.data.otpData
import com.example.electiveselector.data.signupData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class signupViewModel(application: Application): AndroidViewModel(application) {

    var signResponse: MutableLiveData<Response<myResponse>> = MutableLiveData()

    fun addUser(signupData: signupData) {
        retrofitInstance.api.createUser(
            signupData.Name,
            signupData.userName,
            signupData.userEmail,
            signupData.userPassword
        ).enqueue(object : Callback<myResponse> {
            override fun onResponse(
                call: Call<myResponse>,
                response: Response<myResponse>
            ) {
                signResponse.value = response
            }

            override fun onFailure(call: Call<myResponse>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    var otpResponse: MutableLiveData<Response<myResponse>> = MutableLiveData()


    fun postOtp(otpData: otpData) {
        retrofitInstance.api.sendOtp(
            otpData.userName,
            otpData.userEmail
        ).enqueue(object : Callback<myResponse> {
            override fun onResponse(
                call: Call<myResponse>,
                response: Response<myResponse>
            ) {
                otpResponse.value = response
            }

            override fun onFailure(call: Call<myResponse>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}