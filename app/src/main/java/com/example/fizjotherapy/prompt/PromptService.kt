package com.example.fizjotherapy.prompt

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.Callable

class PromptService(private val activity: Activity) {

    fun setErrorTextView(errorView: TextView, text: String?, color: String?, visible: Int) {
        errorView.text = text
        errorView.setTextColor(Color.parseColor(color))
        errorView.visibility = visible
    }

    fun showSuccessToast(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showFailToast(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun buildAlertDialog(
        message: String?,
        positiveAction: () -> Unit,
        negative: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val alertDialog: AlertDialog = builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                positiveAction()
            }
            .setNegativeButton("No") { _, _ ->
                negative()
            }.create()
        alertDialog.show()
    }
}