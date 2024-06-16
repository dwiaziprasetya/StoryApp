package com.example.storyapp.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.storyapp.R

object DialogHelper {


    private fun getCustomFont(context: Context): Typeface? {
        return ResourcesCompat.getFont(context, R.font.poppinsmedium)
    }

    fun showSuccessDialog(
        context: Context,
        title: String?,
        textContent: String?,
    ) {
        showDialog(context, title, textContent, SweetAlertDialog.SUCCESS_TYPE)
    }


    fun showLoadingDialog(
        context: Context,
        title: String?,
        textContent: String?,
        isShowing: Boolean = true
    ) {
        showDialog(context, title, textContent, SweetAlertDialog.PROGRESS_TYPE)
    }

    fun showErrorDialog(
        context: Context,
        title: String?,
        textContent: String?,
    ) {
        showDialog(context, title, textContent, SweetAlertDialog.ERROR_TYPE)
    }


    private fun showDialog(
        context: Context,
        title: String?,
        textContent: String?,
        alertType: Int,
        isShowing: Boolean = true
    ) {
        val customFont = getCustomFont(context)

        val dialog = SweetAlertDialog(context, alertType)
            .setTitleText(title)
            .setContentText(textContent)

        dialog.show()

        val titleText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.title_text)
        val contentText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)

        titleText?.typeface = customFont
        contentText?.typeface = customFont
    }
}