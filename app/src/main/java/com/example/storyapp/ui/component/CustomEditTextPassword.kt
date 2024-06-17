package com.example.storyapp.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupTextChangedListener()
    }

    private fun setupTextChangedListener() {
        addTextChangedListener { text ->
            val parentLayout = parent?.parent as? TextInputLayout
            if (text.toString().length < 8) {
                parentLayout?.error = "Password must be at least 8 characters"
                parentLayout?.isErrorEnabled = true
                parentLayout?.errorIconDrawable = null
            } else {
                parentLayout?.error = null
                parentLayout?.isErrorEnabled = false
            }
        }
    }
}
