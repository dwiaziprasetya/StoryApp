package com.example.storyapp.ui.activity.login

import android.os.Bundle
import android.util.Log
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
    }
}