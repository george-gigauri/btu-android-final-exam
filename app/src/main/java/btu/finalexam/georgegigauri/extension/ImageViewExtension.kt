package btu.finalexam.georgegigauri.extension

import android.widget.ImageView
import btu.finalexam.georgegigauri.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImage(url: String?, placeholder: Int = R.drawable.ic_launcher_background) {
    Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().apply {
                diskCacheStrategy(DiskCacheStrategy.DATA)
                placeholder(placeholder)
                error(placeholder)
            }
        )
        .asDrawable()
        .load(url)
        .into(this)
}