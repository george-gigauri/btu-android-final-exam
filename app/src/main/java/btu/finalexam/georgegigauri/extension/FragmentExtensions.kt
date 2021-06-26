package btu.finalexam.georgegigauri.extension

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(message: String?) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.snackBar(message: String?, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        requireView(),
        message ?: "No message",
        length
    ).show()
}

fun Fragment.snackBar(
    message: String?,
    button: String, onClick: () -> Unit,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(
        requireView(),
        message ?: "No message",
        length
    ).setAction(button) {
        onClick.invoke()
    }.show()
}