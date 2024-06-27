package com.example.storyapp.ui.screen.activity.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.databinding.ActivitySignUpBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.screen.activity.login.LoginActivity
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private var loadingDialog: SweetAlertDialog? = null
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<SignUpViewModel> {factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)


        viewModel.registerResponse.observe(this) { response ->
            if (response.error) {
                DialogHelper.showErrorDialog(
                    this,
                    "Registration Failed",
                    response.message
                )
                Toast.makeText(this, "Registration failed: ${response.message}", Toast.LENGTH_SHORT).show()
            } else {
                DialogHelper.showSuccessDialog(
                    this,
                    "Success",
                    "Account has been created"
                )
                Toast.makeText(this, "Account has been created please login", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(this) { loading ->
            loadingDialog?.setCanceledOnTouchOutside(true)
            if (loading) {
               showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }


        setUpAction()
        playAnimation()
    }

    private fun setUpAction() {
        binding.tvLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@SignUpActivity,
                    LoginActivity::class.java
                )
            )
        }

        binding.btnCreateAccount.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmailAddress.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty()) {
                DialogHelper.showErrorDialog(
                    this,
                    "Username is required",
                    "Please enter your username"
                )
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                DialogHelper.showErrorDialog(
                    this,
                    "Email is required",
                    "Please enter your email"
                )
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                DialogHelper.showErrorDialog(
                    this,
                    "Password is required",
                    "Please enter your password"
                )
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.register(username, email, password)
            }
        }
    }

    private fun playAnimation() {
        val signUp = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(1000)
        val emailAddress = ObjectAnimator.ofFloat(binding.tvEmailAddress, View.ALPHA, 1f).setDuration(1000)
        val emailAddressInput = ObjectAnimator.ofFloat(binding.etEmailAddressLayout, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(1000)
        val passwordInput = ObjectAnimator.ofFloat(binding.etPasswordLayout, View.ALPHA, 1f).setDuration(1000)
        val buttonCreateAccount = ObjectAnimator.ofFloat(binding.btnCreateAccount, View.ALPHA, 1f).setDuration(1000)
        val dontHaveAccount = ObjectAnimator.ofFloat(binding.tvAlreadyHaveAccount, View.ALPHA, 1f).setDuration(1000)
        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playTogether(
                signUp,
                emailAddress,
                emailAddressInput,
                password,
                passwordInput,
                buttonCreateAccount,
                dontHaveAccount,
                login
            )
        }.start()
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