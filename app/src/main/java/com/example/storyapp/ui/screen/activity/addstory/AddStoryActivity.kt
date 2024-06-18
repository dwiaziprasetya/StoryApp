package com.example.storyapp.ui.screen.activity.addstory

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.data.remote.response.FileUploadResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.screen.activity.camera.CameraActivity
import com.example.storyapp.ui.screen.activity.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.storyapp.util.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private var loadingDialog: SweetAlertDialog? = null

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let { image ->
            binding.previewImageView.apply {
                setImageURI(image)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.etDescriptionInput.text.toString()
            showLoadingDialog()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            viewModel.addStoryResponse.observe(this) { response ->
                if (response.error) {
                    DialogHelper.showErrorDialog(
                        this,
                        "Upload Failed",
                        response.message,
                    )
                } else {
                    DialogHelper.showSuccessDialog(
                        this,
                        "Success",
                        "Upload Successful",
                    )
                }
            }

            viewModel.isLoading.observe(this) { loading ->
                if (loading) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            }

            lifecycleScope.launch {
                viewModel.addStory(multipartBody, requestBody)
            }
        } ?: DialogHelper.showErrorDialog(
            this,
            "Upload Failed",
            "Please select an image first"
        )
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

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}