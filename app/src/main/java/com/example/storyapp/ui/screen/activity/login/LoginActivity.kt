package com.example.storyapp.ui.screen.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.databinding.ActivitySignUpBinding
import com.example.storyapp.helper.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: LoginViewModel by viewModels { factory }

        viewModel.loginResponse.observe(this) { response ->
            if (response.error) {
                Toast.makeText(this, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                Log.e("ERROR", response.message)
            } else {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailAddress.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            lifecycleScope.launch {
                viewModel.login(email, password)
            }
        }

        playAnimation()
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
}