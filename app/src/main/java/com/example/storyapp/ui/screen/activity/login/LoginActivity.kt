package com.example.storyapp.ui.screen.activity.login

import SessionPreferences
import SessionViewModelFactory
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.screen.activity.main.MainActivity
import com.example.storyapp.ui.screen.activity.main.MainViewModel
import com.example.storyapp.ui.screen.activity.signup.SignUpActivity
import dataStore
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loadingDialog: SweetAlertDialog? = null
    private lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<LoginViewModel> {factory}
    private lateinit var sessionViewModel : MainViewModel
    private lateinit var pref : SessionPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SessionPreferences.getInstance(application.dataStore)
        sessionViewModel = ViewModelProvider(
            this,
            SessionViewModelFactory(pref)
        )[MainViewModel::class.java]

        factory = ViewModelFactory.getInstance(this)

        viewModel.loginResponse.observe(this) { response ->
            if (response.error) {
                DialogHelper.showErrorDialog(
                    this,
                    "Login Failed",
                    response.message,
                )
                Toast.makeText(this, "Registration failed: ${response.message}", Toast.LENGTH_SHORT).show()
            } else {
                DialogHelper.showSuccessDialog(
                    this,
                    "Success",
                    "Login Successful",
                    navigateTo = {
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        )
                    }
                )
                sessionViewModel.saveLoginData(response)
            }
        }

        viewModel.loading.observe(this) { loading ->
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
        binding.tvSignUp.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignUpActivity::class.java)
            )
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailAddress.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

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
                viewModel.login(email, password)
            }
        }
    }

    private fun playAnimation(){
        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(1000)
        val emailAddress = ObjectAnimator.ofFloat(binding.tvEmailAddress, View.ALPHA, 1f).setDuration(1000)
        val emailAddressInput = ObjectAnimator.ofFloat(binding.etEmailAddressLayout, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(1000)
        val passwordInput = ObjectAnimator.ofFloat(binding.etPasswordLayout, View.ALPHA, 1f).setDuration(1000)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(1000)
        val dontHaveAccount = ObjectAnimator.ofFloat(binding.tvDoYouHaveAccount, View.ALPHA, 1f).setDuration(1000)
        val signUp = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playTogether(
                welcome,
                emailAddress,
                emailAddressInput,
                password,
                passwordInput,
                buttonLogin,
                dontHaveAccount,
                signUp
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