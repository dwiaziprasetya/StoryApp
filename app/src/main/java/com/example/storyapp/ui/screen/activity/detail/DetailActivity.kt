package com.example.storyapp.ui.screen.activity.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.screen.activity.signup.SignUpViewModel
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private var loadingDialog: SweetAlertDialog? = null
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<DetailViewModel> {factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.dark(
                    android.graphics.Color.TRANSPARENT
                )
        )
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        if (id.isNullOrEmpty()) {
            Toast.makeText(this, "Story ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        factory = ViewModelFactory.getInstance(this)

        viewModel.detailStory.observe(this) { response ->
            setData(response)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            Log.d("woi", isLoading.toString())
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }
    }

    private fun setData(story: StoryResponse) {
        binding.tvDescription.text = story.listStory.first().description
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