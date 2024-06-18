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
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.repository.UserRepository
import com.example.storyapp.ui.adapter.StoryAdapter
import com.example.storyapp.ui.screen.activity.addstory.AddStoryActivity
import com.example.storyapp.ui.screen.activity.welcome.WelcomeActivity
import dataStore
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var loadingDialog: SweetAlertDialog? = null
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        val pref = SessionPreferences.getInstance(applicationContext.dataStore)
        val repository = Injection.provideRepository(applicationContext)
        SessionViewModelFactory(pref, repository)
    }
    private lateinit var storyAdapter: StoryAdapter


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

        storyAdapter = StoryAdapter()
        binding.rvStoryList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }

        viewModel.stories.observe(this) { response ->
            if (response.error) {
                DialogHelper.showErrorDialog(
                    this,
                    "Stories Failed",
                    response.message,
                )
            } else {
                storyAdapter.submitList(response.listStory)
                DialogHelper.showSuccessDialog(
                    this,
                    "Success",
                    "Stories Loaded",
                )
            }
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
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

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogHelper.showLoadingDialog(this)
        } else if (!loadingDialog!!.isShowing) {
            loadingDialog!!.show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismissWithAnimation()
            }
        }
    }
}