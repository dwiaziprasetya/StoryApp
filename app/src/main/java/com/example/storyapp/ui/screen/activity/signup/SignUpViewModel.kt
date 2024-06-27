package com.example.storyapp.ui.screen.activity.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    suspend fun register(
        username: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val register = userRepository.register(username, email, password)
                _isLoading.value = false
                _registerResponse.value = register
            } catch (e: Exception) {
                _registerResponse.value = RegisterResponse(
                    error = true,
                    message = e.message.toString()

                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}