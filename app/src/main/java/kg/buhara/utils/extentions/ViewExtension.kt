package kg.buhara.utils.extentions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun EditText.validate(
    validator: (String) -> Boolean,
    message: String
): Boolean {
    val flag = validator(this.text.toString())
    this.error = if (flag) null else message

    return flag
}

fun TextView.validate(
    validator: (String) -> Boolean,
    message: String
): Boolean {
    val flag = validator(this.text.toString())
    this.error = if (flag) null else message

    return flag
}

fun EditText.text(
    message: String?
) {
    if (!message.isNullOrEmpty()) {
        this.setText(message)
    }
}

fun EditText.cursorToEnd(
) {
    setSelection(text.toString().length)
}

fun TextView.text(
    message: String?
) {
    if (!message.isNullOrEmpty()) this.text = message
}

fun EditText.toInteger(
    message: String?
) {
    if (!message.isNullOrEmpty()) {
        this.setText(message)
    }
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(this, message, duration).apply {
        show()
    }

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(this, resId, duration).apply {
        show()
    }

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity?.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.actionCall(phone: String) {
    val dialIntent = Intent(Intent.ACTION_DIAL)
    dialIntent.data = Uri.parse("tel:$phone")
    startActivity(dialIntent)
}


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun parseDateFromServer(inputDate: String?): String? {
    val DATE_FORMAT_I = "yyyy-MM-dd"
    val DATE_FORMAT_O = "dd-MM-yyyy"
    val formatInput = SimpleDateFormat(DATE_FORMAT_I)
    val formatOutput = SimpleDateFormat(DATE_FORMAT_O)
    var date: Date? = null
    try {
        date = formatInput.parse(inputDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formatOutput.format(date)
}



fun parseDateToServer(inputDate: String?): String {
    val DATE_FORMAT_I = "dd-MM-yyyy"
    val DATE_FORMAT_O = "yyyy-MM-dd"
    val formatInput = SimpleDateFormat(DATE_FORMAT_I)
    val formatOutput = SimpleDateFormat(DATE_FORMAT_O)
    var date: Date? = null
    try {
        date = formatInput.parse(inputDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formatOutput.format(date)
}
