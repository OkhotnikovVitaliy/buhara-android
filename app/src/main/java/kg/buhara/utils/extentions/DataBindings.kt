package kg.buhara.utils.extentions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import kg.buhara.utils.imageloader.loadImage


/**
 * Created by Amir Hossein Ghasemi since 3/11/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

@BindingAdapter("srcUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (imageUrl == null) return
    loadImage(imageUrl, view)
}