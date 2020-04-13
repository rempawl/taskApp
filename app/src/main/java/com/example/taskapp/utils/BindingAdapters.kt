package com.example.taskapp.utils

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("setError")
fun setError(view: EditText, stringOrRsrcID: Any?) {
    if (stringOrRsrcID != null) {
        when (stringOrRsrcID) {
            is String -> view.error = stringOrRsrcID
            is Int -> view.apply {
                val text = context.resources.getString(stringOrRsrcID)
                error = text
            }
        }
    }

}

@BindingAdapter("onFocus")
fun bindFocusChange(
    editText: EditText,
    onFocusChangeListener: View.OnFocusChangeListener?
) {
    onFocusChangeListener ?: return
    editText.onFocusChangeListener = onFocusChangeListener

}
