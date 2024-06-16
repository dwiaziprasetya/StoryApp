package com.example.storyapp.ui.activity.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.repository.StroryAppRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val storyAppRepository: StroryAppRepository
) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    suspend fun register(
        username: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val register = storyAppRepository.register(username, email, password)
                _registerResponse.value = register
            } catch (e: Exception) {
                _registerResponse.value = RegisterResponse(error = true, message = e.message.toString())
            }
        }
    }
}