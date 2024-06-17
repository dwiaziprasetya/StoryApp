package com.example.storyapp.ui.screen.activity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    suspend fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val login = userRepository.login(email, password)
                _loginResponse.value = login
                _isLoading.value = false
            } catch (e: Exception) {
                _loginResponse.value = LoginResponse(
                    error = true,
                    message = e.message.toString(),
                    loginResult = LoginResult(
                        name = "",
                        userId = "",
                        token = ""
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }

    }
}