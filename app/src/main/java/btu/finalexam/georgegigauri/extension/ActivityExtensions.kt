package btu.finalexam.georgegigauri.extension

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

fun Activity.openActivity(activity: Class<*>) {
    startActivity(Intent(this, activity))
}