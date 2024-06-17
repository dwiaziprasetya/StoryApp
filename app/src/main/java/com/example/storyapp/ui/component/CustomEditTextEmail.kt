package com.example.storyapp.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditTextEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validateEmail()
            }

        })
    }

    private fun validateEmail() {
        val emailInput = text.toString()
        val parentLayout = parent.parent as? TextInputLayout

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            parentLayout?.error = "Invalid email address"
        } else {
            parentLayout?.error = null
        }
    }
}