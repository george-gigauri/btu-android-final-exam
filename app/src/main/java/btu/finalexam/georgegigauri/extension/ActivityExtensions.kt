package btu.finalexam.georgegigauri.extension

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Activity.toast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.snackBar(view: View, message: String?, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        view,
        message ?: "No message",
        length
    ).show()
}

fun Activity.snackBar(
    view: View, message: String?,
    button: String, onClick: () -> Unit,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(
        view,
        message ?: "No message",
        length
    ).setAction(button) {
        onClick.invoke()
    }.show()
}