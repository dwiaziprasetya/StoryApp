package com.example.storyapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.di.Injection
import com.example.storyapp.repository.UserRepository
import com.example.storyapp.ui.screen.activity.addstory.AddStoryViewModel
import com.example.storyapp.ui.screen.activity.detail.DetailViewModel
import com.example.storyapp.ui.screen.activity.gmaps.MapsViewModel
import com.example.storyapp.ui.screen.activity.login.LoginViewModel
import com.example.storyapp.ui.screen.activity.main.MainViewModel
import com.example.storyapp.ui.screen.activity.signup.SignUpViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val id: String = ""
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(userRepository, id) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        fun getInstance(context: Context, id: String = "") = ViewModelFactory(Injection.provideRepository(context), id)
    }
}