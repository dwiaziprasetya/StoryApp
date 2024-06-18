package com.example.storyapp.ui.screen.activity.main

import SessionPreferences
import SessionViewModelFactory
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.screen.activity.addstory.AddStoryActivity
import com.example.storyapp.ui.screen.activity.welcome.WelcomeActivity
import dataStore
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        SessionViewModelFactory(SessionPreferences.getInstance(applicationContext.dataStore))
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) {
            if (it.token.isEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

//        binding.btnAddStory.setOnClickListener {
//            startActivity(Intent(this, AddStoryActivity::class.java))
//        }
//
//        binding.btnLogout.setOnClickListener {
//            viewModel.logout()
//        }
//
//        lifecycleScope.launch {
//            viewModel.getName().collect { name ->
//                binding.tvWelcomeUser.text = "Hello $name"
//            }
//        }
    }
}