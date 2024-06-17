package com.example.storyapp.ui.screen.activity.detail

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapp.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.dark(
                    android.graphics.Color.TRANSPARENT
                )
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}