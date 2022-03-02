package com.vlad.finboard

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Locale

fun Fragment.toast(text: String) {
    Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
}

fun Activity.hideSoftKeyboard(editText: View) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(editText.windowToken, 0)
    }
}

fun Long.millisToDate(): String {
    val targetFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    return targetFormat.format(this)
}