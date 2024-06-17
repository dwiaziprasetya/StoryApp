package com.example.storyapp.ui.screen.activity.main

import SessionPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SessionPreferences) : ViewModel() {

    fun saveLoginData(loginResponse: LoginResponse) {
        viewModelScope.launch {
            pref.saveLoginData(loginResponse)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return pref.getSession().asLiveData()
    }

    fun getName(): Flow<String?> {
        return pref.getName()
    }
}