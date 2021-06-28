package btu.finalexam.georgegigauri.extension

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

fun <T> getDiffUtil() = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}