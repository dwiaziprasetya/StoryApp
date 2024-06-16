package com.example.storyapp.ui.screen.activity.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.databinding.ActivitySignUpBinding
import com.example.storyapp.helper.ViewModelFactory
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: SignUpViewModel by viewModels { factory }

        viewModel.registerResponse.observe(this) { response ->
            if (response.error) {
                Toast.makeText(this, "Registration failed: ${response.message}", Toast.LENGTH_SHORT).show()
                Log.e("ERROR", response.message)
            } else {
                showAlert()
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmailAddress.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            lifecycleScope.launch {
                viewModel.register(username, email, password)
            }
        }

        playAnimation()
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

    private fun showAlert() {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage("Account has been created")
            create()
            show()
        }
    }
}