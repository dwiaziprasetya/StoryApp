package com.example.storyapp.ui.screen.activity.main

import SessionPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: SessionPreferences,
    private val repository: UserRepository
) : ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> get() = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            getStories()
        }
    }

    private suspend fun getStories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val story = repository.getStories()
                _stories.value = story
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveLoginData(loginResponse: LoginResponse) {
        viewModelScope.launch {
            pref.saveLoginData(loginResponse)
        }
    }



//    fun getStories() {
//        viewModelScope.launch {
//            repository.getStories()
//        }
//    }

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