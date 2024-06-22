package com.example.storyapp.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        navigateTo: () -> Unit = {}
    ) {
        showDialog(
            context,
            title,
            textContent,
            SweetAlertDialog.SUCCESS_TYPE,
            navigateTo
        )
    }


    fun showLoadingDialog(
        context: Context
    ): SweetAlertDialog {
        val customFont = getCustomFont(context)

        return SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE).apply {
            setTitleText("Please wait")
            progressHelper.barColor = ContextCompat.getColor(context, R.color.purpleOcean)
            show()
            setCancelable(true)

            val titleText = findViewById<TextView>(cn.pedant.SweetAlert.R.id.title_text)
            val contentText = findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)

            titleText?.typeface = customFont
            contentText?.typeface = customFont
        }
    }

    fun showErrorDialog(
        context: Context,
        title: String?,
        textContent: String?,
        navigateTo: () -> Unit = {}
    ) {
        showDialog(
            context,
            title,
            textContent,
            SweetAlertDialog.ERROR_TYPE,
            navigateTo
        )
    }

    private fun showDialog(
        context: Context,
        title: String?,
        textContent: String?,
        alertType: Int,
        navigateTo: () -> Unit = {}
    ) {
        val customFont = getCustomFont(context)

        val dialog = SweetAlertDialog(context, alertType)
            .setTitleText(title)
            .setConfirmText("OK")
            .setContentText(textContent)
            .setConfirmClickListener {
                navigateTo()
                it.dismissWithAnimation()
            }

        dialog.setCanceledOnTouchOutside(true)

        dialog.show()

        val titleText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.title_text)
        val contentText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)
        val confirmButton = dialog.findViewById<Button>(cn.pedant.SweetAlert.R.id.confirm_button)


        titleText?.typeface = customFont
        contentText?.typeface = customFont
        confirmButton?.typeface = customFont

        confirmButton.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purpleOcean)
    }
}