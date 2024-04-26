package com.example.fizjotherapy.prompt

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.Callable

class PromptService(private val context: Context) {

    fun setErrorTextView(errorView: TextView, text: String?, color: String?, visible: Int) {
        errorView.text = text
        errorView.setTextColor(Color.parseColor(color))
        errorView.visibility = visible
    }

    fun showSuccessToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showFailToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun buildAlertDialog(
        activity: Activity,
        message: String?,
        positiveAction: Callable<Void?>,
        negative: Callable<Void?>
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val alertDialog: AlertDialog = builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                try {
                    positiveAction.call()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
            .setNegativeButton("No") { _, _ ->
                try {
                    negative.call()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }.create()
        alertDialog.show()
    }
}