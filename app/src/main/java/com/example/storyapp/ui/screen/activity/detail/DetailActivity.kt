package com.example.storyapp.ui.screen.activity.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private var loadingDialog: SweetAlertDialog? = null
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<DetailViewModel> {factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")

        factory = ViewModelFactory.getInstance(this, id.toString())

        viewModel.detailStory.observe(this) { storyResponse ->
            if (storyResponse.error) {
                DialogHelper.showErrorDialog(
                    this,
                    "Stories Failed",
                    storyResponse.message,
                )
            } else {
                setData(storyResponse.story)
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

        binding.tlbrDetailStory.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }
    }


    private fun setData(story: Story){
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(imageStory)
            storyTitle.text = story.name
            tvDescription.text = story.description
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