package com.example.storyapp.ui.screen.activity.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.FileUploadResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addStoryResponse = MutableLiveData<FileUploadResponse>()
    val addStoryResponse: LiveData<FileUploadResponse> = _addStoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val addStory = userRepository.uploadImage(file, description)
                _addStoryResponse.value = addStory
                _isLoading.value = false
            } catch (e: Exception) {
                _addStoryResponse.value = FileUploadResponse(
                    error = true,
                    message = e.message.toString()
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}