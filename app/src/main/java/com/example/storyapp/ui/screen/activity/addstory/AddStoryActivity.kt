@file:Suppress("DEPRECATION")

package com.example.storyapp.ui.screen.activity.addstory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
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
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.screen.activity.camera.CameraActivity
import com.example.storyapp.ui.screen.activity.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.storyapp.ui.screen.activity.main.MainActivity
import com.example.storyapp.util.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private var loadingDialog: SweetAlertDialog? = null
    private var isLocationAdded: Boolean = false

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLastLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        with(binding) {
            tlbrAddStory.setNavigationOnClickListener {
                onBackPressed()
                isLocationAdded = false
            }
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { checkAndRequestCameraPermission() }
            uploadButton.setOnClickListener { uploadImage() }
            tlbrAddStory.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add_location -> {
                        showLocationConfirmationDialog()
                        true
                    }
                    R.id.reset_location -> {
                        isLocationAdded = false
                        binding.tvLocation.text = ""
                        Toast.makeText(this@AddStoryActivity, "location deleted", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }
    }


    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getLastLocation()
        }
    }

    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            startCamera()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        isLocationAdded = false
    }

    @SuppressLint("SetTextI18n")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        lastLocation = it
                        isLocationAdded = true
                        binding.tvLocation.text = "Lat : ${lastLocation.latitude} Lon : ${lastLocation.longitude} "
                    }
                }
        }
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
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
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
            val description = binding.etDescriptionInput.text.toString()
            showLoadingDialog()

            val lat = if (isLocationAdded) {
                lastLocation.latitude.toString().toRequestBody("text/plain".toMediaType())
            } else {
                null
            }

            val lon = if (isLocationAdded) {
                lastLocation.longitude.toString().toRequestBody("text/plain".toMediaType())
            } else {
                null
            }


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
                        navigateTo = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
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
                viewModel.addStory(multipartBody, requestBody, lat, lon)
                isLocationAdded = false
            }
        } ?: DialogHelper.showErrorDialog(
            this,
            "Upload Failed",
            "Please select an image first"
        )
    }

    private fun showLocationConfirmationDialog() {
        DialogHelper.showWarningDialog(
            context = this,
            title = "Add Location",
            textContent = "Do you want to add your current location to the story?",
            navigateTo = {
                checkAndRequestLocationPermission()
                Toast.makeText(this, "Get current location", Toast.LENGTH_SHORT).show()
            },
            setCancelable = {},
            setCancel = true
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
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
