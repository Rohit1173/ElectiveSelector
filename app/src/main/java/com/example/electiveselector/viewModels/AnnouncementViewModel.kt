package com.example.electiveselector.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.electiveselector.api.retrofitInstance
import com.example.electiveselector.data.AnnouncementResponse
import kotlinx.coroutines.launch

class AnnouncementViewModel(application: Application):AndroidViewModel(application) {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _announcements = MutableLiveData<AnnouncementResponse>()
    val announcements: LiveData<AnnouncementResponse> = _announcements

init{
    getAnnouncements()
}

    private fun getAnnouncements() {


        viewModelScope.launch {
            try {

                _announcements.value =
                    retrofitInstance.api.announcements()
                _status.value = "SUCCESS"


            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
                Log.d("hitman", _status.value.toString())
            }
        }

    }
}