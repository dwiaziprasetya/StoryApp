package com.example.storyapp.ui.screen.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.adapter.StoryAdapter
import com.example.storyapp.ui.screen.activity.addstory.AddStoryActivity
import com.example.storyapp.ui.screen.activity.detail.DetailActivity
import com.example.storyapp.ui.screen.activity.welcome.WelcomeActivity

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
            if (it.name == "") {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
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
            }
        }

        storyAdapter = StoryAdapter()
        binding.rvStoryList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }

        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemCallback {
            override fun onItemClicked(story: ListStoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", story.id)
                startActivity(intent)
            }

        })


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