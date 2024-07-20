package com.example.storyapp.helper

import android.content.Context
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
            navigateTo = navigateTo
        )
    }

    fun showWarningDialog(
        context: Context,
        title: String?,
        textContent: String?,
        navigateTo: () -> Unit = {},
        setCancelable: () -> Unit,
        setCancel: Boolean
    ) {
        showDialog(
            context,
            title,
            textContent,
            SweetAlertDialog.WARNING_TYPE,
            navigateTo = navigateTo,
            setCancelable = setCancelable,
            setCancel = setCancel
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
            navigateTo = navigateTo
        )
    }

    private fun showDialog(
        context: Context,
        title: String?,
        textContent: String?,
        alertType: Int,
        setCancel: Boolean = false,
        navigateTo: () -> Unit = {},
        setCancelable: () -> Unit = {}
    ) {
        val customFont = getCustomFont(context)

        val dialog = SweetAlertDialog(context, alertType)
            .setTitleText(title)
            .setConfirmText("Ok")
            .setContentText(textContent)
            .setConfirmClickListener {
                navigateTo()
                it.dismissWithAnimation()
            }

        if (setCancel) {
            dialog.setCancelClickListener {
                setCancelable()
                it.dismissWithAnimation()
            }.setCancelText("Cancel")
        }

        dialog.setCanceledOnTouchOutside(true)

        dialog.show()

        val titleText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.title_text)
        val contentText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)
        val confirmButton = dialog.findViewById<Button>(cn.pedant.SweetAlert.R.id.confirm_button)
        val cancelButton = dialog.findViewById<Button>(cn.pedant.SweetAlert.R.id.cancel_button)


        titleText?.typeface = customFont
        contentText?.typeface = customFont
        confirmButton?.typeface = customFont
        cancelButton?.typeface = customFont

        confirmButton.backgroundTintList = ContextCompat.getColorStateList(context, R.color.purpleOcean)
    }
}