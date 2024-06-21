package com.example.storyapp.ui.screen.activity.main

import com.example.storyapp.util.SessionPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getStories()
    }

    fun getStories() {
        Log.d("MainViewModel!!!!!!!!!!!!!!!!!!!!!!!!", "getStories called")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("MainViewModel!!!!!!!!!!!!!!!!!!!!!!!!", "try called")
                val story = repository.getStories()
                _stories.value = story
            } catch (e: Exception) {
                Log.d("MainViewModel!!!!!!!!!!!!!!!!!!!!!!!!", "catch called")
                _stories.value = StoryResponse(
                    error = true,
                    message = e.message.toString(),
                    listStory = emptyList()
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}