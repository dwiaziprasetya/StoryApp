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

    suspend fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val login = userRepository.login(email, password)
                _loginResponse.value = login
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
            }
        }

    }
}