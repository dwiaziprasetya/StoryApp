package com.example.storyapp.ui.screen.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.adapter.StoryAdapter
import com.example.storyapp.ui.screen.activity.addstory.AddStoryActivity
import com.example.storyapp.ui.screen.activity.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var loadingDialog: SweetAlertDialog? = null
    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getSession().observe(this) {
            val token = it.token
            Log.d("MainActivity", "Token dari session: $token")
            if (it.name == "") {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        viewModel.stories.observe(this) { response ->
            if (response.error) {
                Log.e("Hei", response.message)
                DialogHelper.showErrorDialog(
                    this,
                    "Stories Failed",
                    response.message,
                )
            } else {
                storyAdapter.submitList(response.listStory)
            }
        }

        storyAdapter = StoryAdapter()
        binding.rvStoryList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }


        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.tlbrListStory.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.logout_menu -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            Log.d("Loading hey", isLoading.toString())
            loadingDialog?.setCanceledOnTouchOutside(true)
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
                DialogHelper.showSuccessDialog(
                    this,
                    "Success",
                    "Stories Loaded",
                    navigateTo = {
                        loadingDialog?.dismiss()
                    }
                )
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getStories()
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